package com.example.excluzscheduler.domain.store.storeSettlement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.excluzscheduler.common.entity.StoreSettlement;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRevenue.repository.StoreRevenueRepository;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.FeeRate;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.SettlementStatus;
import com.example.excluzscheduler.domain.store.storeSettlement.repository.StoreSettlementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSettlementService {

	private final StoreSettlementRepository settlementRepository;
	private final StoreRevenueRepository revenueRepository;

	@Transactional
	public void createSettlement(
		RevenuePeriod settlementsPeriod, LocalDateTime startDateTime, LocalDateTime endDateTime
	) {
		// 이미 존재하는 정산 내역 조회 -> key-value 형태로 저장
		List<StoreSettlement> existingSettlementList = settlementRepository.findByPeriod(startDateTime, endDateTime, settlementsPeriod);
		Map<Integer, StoreSettlement> settlementMap = existingSettlementList.stream()
			.collect(Collectors.toMap(StoreSettlement::getStoreId, Function.identity()));

		// 기간별 매출 합계를 스토어별로 조회
		List<Object[]> storeRevenueList = revenueRepository.findTotalRevenueWithPeriod(startDateTime, endDateTime, settlementsPeriod);
		List<StoreSettlement> settlementList = new ArrayList<>();

		// 정산 시작
		for (Object[] row : storeRevenueList) {
			int storeId = (int)row[0];
			long totalRevenue = (long)row[1];

			StoreSettlement storeSettlement = settlementMap.get(storeId);

			// 이미 정산 완료된 건은 갱신하지 않음
			if (storeSettlement != null && storeSettlement.getSettlementStatus().equals(SettlementStatus.COMPLETED)) {
				continue;
			}

			// 플랫폼 수수료 기본 10%로 계산 (TODO 수수료 산정 방식 지정 필요)
			FeeRate feeRate = FeeRate.MEDIUM;
			long platformFeeAmount = (long)(feeRate.getFeeRate() * totalRevenue);
			long settlementAmount = totalRevenue - platformFeeAmount;

			// 완료되지 않은 정산 기록이 존재할 경우 갱신
			if (storeSettlement != null) {
				storeSettlement.updateStoreSettlement(totalRevenue, feeRate, settlementAmount);
			}
			// 정산 기록이 없을 경우 새로 생성
			if (storeSettlement == null) {
				StoreSettlement newStoreSettlement = StoreSettlement.builder()
					.storeId(storeId)
					.totalRevenue(totalRevenue)
					.settlementAmount(settlementAmount)
					.platformFeeRate(feeRate)
					.settlementPeriod(settlementsPeriod)
					.startDate(startDateTime)
					.endDate(endDateTime)
					.build();

				settlementList.add(newStoreSettlement);
			}
		}
		settlementRepository.saveAll(settlementList);
	}
}
