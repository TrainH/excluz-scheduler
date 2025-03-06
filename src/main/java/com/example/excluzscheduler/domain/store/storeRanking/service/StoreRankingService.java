package com.example.excluzscheduler.domain.store.storeRanking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.excluzscheduler.common.entity.Store;
import com.example.excluzscheduler.common.entity.StoreRanking;
import com.example.excluzscheduler.domain.store.store.repository.StoreRepository;
import com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto;
import com.example.excluzscheduler.domain.store.storeRanking.repository.StoreRankingRepository;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRevenue.repository.StoreRevenueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreRankingService {
	private final StoreRevenueRepository storeRevenueRepository;
	private final StoreRankingRepository storeRankingRepository;
	private final StoreRepository storeRepository;

	// 매일 자정마다 실행되는 스케줄러 (TOP 10 랭킹 업데이트)
	/**
	 * 1. 매출이 동일할 경우 동일한 등수로 표시
	 * 2. 매출이 동일하지 않은 이후 스토어가 있는 경우, (동일한 등수 + 동일한 스토어의 수) 만큼의 등수를 얻는다 -> 1등이 2개면, 다음 등수는 3등 처리
	 */
	@Transactional
	public void updateStoreRankings(
		RevenuePeriod rankingPeriod, LocalDateTime startDateTime, LocalDateTime endDateTime
	) {
		log.info("🔍 매출 기준 TOP10 가져오는 중...");

		// 스토어별 매출 합계를 한 번에 조회
		List<Object[]> storeRevenueList = storeRevenueRepository.findTotalRevenueWithPeriod(startDateTime, endDateTime, rankingPeriod);

		if (storeRevenueList.isEmpty()) {
			log.info("⚠️ 매출 데이터가 없습니다. 랭킹을 업데이트하지 않습니다.");
			throw new IllegalStateException("매출 데이터가 존재하지 않아 랭킹을 업데이트할 수 없습니다."); // todo 추후 커스텀 익셉션 처리하기
		}

		// 기존 랭킹 데이터 조회 및 Map 변환 (N+1 방지)
		List<StoreRanking> existingRankings = storeRankingRepository.findByRankingPeriod(rankingPeriod, PageRequest.of(0, 1000)).getContent();
		Map<Integer, StoreRanking> rankingMap = existingRankings.stream()
			.collect(Collectors.toMap(ranking -> ranking.getStore().getId(), Function.identity()));

		// 스토어 ID로 Store 정보 한 번에 조회 (N+1 방지)
		List<Integer> storeIdList = storeRevenueList.stream()
			.map(row -> (Integer) row[0])
			.collect(Collectors.toList());
		Map<Integer, Store> storeMap = storeRepository.findByIdIn(storeIdList).stream()
			.collect(Collectors.toMap(Store::getId, Function.identity()));

		// 랭킹 계산을 위한 변수들
		List<StoreRanking> newRankings = new ArrayList<>();
		int currentRank = 1;
		Long previousRevenue = -1L;
		int previousRank = currentRank;

		for (Object[] row : storeRevenueList) {
			Integer storeId = (Integer) row[0];
			Long totalRevenue = (Long) row[1];

			Store store = storeMap.get(storeId);
			if (store == null) {
				log.warn("⚠️ 존재하지 않는 스토어 ID: {}", storeId);
				continue;
			}

			// 동점 처리 로직
			if (previousRevenue.equals(totalRevenue)) {
				currentRank = previousRank; // 같은 매출이면 같은 랭킹 유지
			} else {
				previousRank = currentRank; // 다른 매출이 나오면 새로운 랭킹 적용
			}

			StoreRanking existingRanking = rankingMap.get(storeId);

			if (existingRanking != null) {
				// 기존 랭킹 업데이트
				existingRanking.updateRank(currentRank, totalRevenue);
			} else {
				// 새로운 랭킹 생성
				StoreRanking newRanking = StoreRanking.builder()
					.store(store)
					.rankingPeriod(rankingPeriod)
					.rankPosition(currentRank)
					.revenue(totalRevenue)
					.build();
				newRankings.add(newRanking);
			}

			previousRevenue = totalRevenue;
			currentRank = previousRank + 1; // 다음 순위는 "현재 랭킹 + 1"이지만, 동점이면 그대로 유지
		}

		// 한 번에 저장 (N+1 방지)
		storeRankingRepository.saveAll(newRankings);

		log.info("✅ 매출 기준 TOP10 가져오기 완료!");
	}

	// TOP 10 랭킹 조회 (매출 정보 제외)
	@Transactional(readOnly = true)
	public List<StoreRankingTop10ResponseDto> getTop10StoreRankings() {
		return storeRankingRepository.findTop10ByRankingPeriod(RevenuePeriod.MONTH, PageRequest.of(0, 10))
			.getContent()
			.stream()
			.map(ranking -> new StoreRankingTop10ResponseDto(
				ranking.getStore().getId(),
				ranking.getStore().getStoreName(),
				ranking.getRankPosition()))
			.collect(Collectors.toList());
	}
}