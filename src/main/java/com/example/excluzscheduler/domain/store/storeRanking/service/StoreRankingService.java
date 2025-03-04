package com.example.excluzscheduler.domain.store.storeRanking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.excluzscheduler.common.entity.Store;
import com.example.excluzscheduler.common.entity.StoreRanking;
import com.example.excluzscheduler.common.entity.StoreRevenue;
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
	public void updateDailyStoreRankings(RevenuePeriod rankingPeriod, LocalDateTime startDatetime, LocalDateTime endDatetime) {
		log.info("🔍 매출 기준 TOP10 가져오는 중...");

		// 어제 매출 기준으로 TOP 10 스토어 조회
		List<StoreRevenue> topStores = storeRevenueRepository.findStoresByRevenue(startDatetime, endDatetime);

		if (topStores.isEmpty()) {
			log.info("⚠️ 매출 데이터가 없습니다. 랭킹을 업데이트하지 않습니다.");
			return;
		}

		int currentRank = 1;

		// 이전 매출 저장
		Long preStoreRevenue = -1L; // 초기화

		int preStoreRank = currentRank;

		for (StoreRevenue revenue : topStores) {
			Store store = storeRepository.findById(revenue.getStoreId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어입니다.")); // todo 추후 커스텀 익셉션 처리

			int rank = currentRank;

			if (preStoreRevenue.equals(revenue.getTotalRevenue())) {
				rank = preStoreRank;
			}

			// 기존 랭킹 존재 여부 확인
			StoreRanking existingRanking = storeRankingRepository.findByStoreAndRankingPeriod(store, rankingPeriod)
				.orElse(null);

			if (existingRanking != null) {
				// 기존 랭킹 업데이트
				existingRanking.updateRank(rank, revenue.getTotalRevenue());
			} else {
				// 새로운 랭킹 생성
				StoreRanking newRanking = StoreRanking.builder()
					.store(store)
					.rankingPeriod(rankingPeriod)
					.rankPosition(rank)
					.revenue(revenue.getTotalRevenue())
					.build();

				storeRankingRepository.save(newRanking);
			}

			preStoreRevenue = revenue.getTotalRevenue();
			preStoreRank = rank;

			currentRank++;
		}

		log.info("✅ 매출 기준 TOP10 가져오기 완료!");
	}

	// TOP 10 랭킹 조회 (매출 정보 제외)
	@Transactional(readOnly = true)
	public List<StoreRankingTop10ResponseDto> getTop10StoreRankings() {
		return storeRankingRepository.findTop10ByRankingPeriod(RevenuePeriod.MINUTE_1, PageRequest.of(0, 10))
			.getContent()
			.stream()
			.map(ranking -> new StoreRankingTop10ResponseDto(
				ranking.getStore().getId(),
				ranking.getStore().getStoreName(),
				ranking.getRankPosition()))
			.collect(Collectors.toList());
	}
}