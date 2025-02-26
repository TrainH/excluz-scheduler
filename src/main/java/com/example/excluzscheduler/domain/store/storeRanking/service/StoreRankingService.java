package com.example.excluzscheduler.domain.store.storeRanking.service;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.excluzscheduler.common.entity.Store;
import com.example.excluzscheduler.common.entity.StoreRanking;
import com.example.excluzscheduler.domain.store.store.repository.StoreRepository;
import com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto;
import com.example.excluzscheduler.domain.store.storeRanking.repository.StoreRankingRepository;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreRankingService {
	private final StoreRankingRepository storeRankingRepository;
	private final StoreRepository storeRepository;

	// 공식 랭킹 TOP 10: 인증 X (누구나 확인 가능)
	@Transactional
	public List<StoreRankingTop10ResponseDto> updateDailyRankings() {
		// 매출 기준으로 TOP 10 조회
		List<StoreRankingTop10ResponseDto> topStores = storeRankingRepository.findTop10StoresForPublic(
			RevenuePeriod.D, PageRequest.of(0, 10)
		);

		// 랭킹 계산 및 저장 (기존 데이터가 있으면 업데이트)
		IntStream.range(0, topStores.size()).forEach(i -> {
			StoreRankingTop10ResponseDto dto = topStores.get(i);
			Store store = storeRepository.findById(dto.getStoreId())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스토어 ID: " + dto.getStoreId()));

			StoreRanking ranking = storeRankingRepository.findByStoreAndPeriod(store, RevenuePeriod.D)
				.map(existingRanking -> {
					existingRanking.updateRank(i + 1, dto.getRevenue()); // 기존 랭킹 업데이트
					return existingRanking;
				})
				.orElse(StoreRanking.builder() // 새로운 랭킹 생성
					.store(store)
					.rankingPeriod(RevenuePeriod.D)
					.rankPosition(i + 1)
					.revenue(dto.getRevenue())
					.build());

			storeRankingRepository.save(ranking);
		});

		return topStores;
	}
}
