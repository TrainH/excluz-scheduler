package com.example.excluzscheduler.domain.store.storeSettlement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.excluzscheduler.common.entity.StoreSettlement;
import com.example.excluzscheduler.domain.store.storeRevenue.repository.StoreRevenueRepository;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.FeeRate;
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
	public void createMonthlySettlement(int currentMonth) {
		// 로직 흐름
		// currentMonth = 현재 월, 현재말고, 직전 월 매출을 정산해야한다. 직전 월 = currentMonth-1
		// 스토어-레비뉴에서 월별(currentMonth-1) 매출액 집계 목록을 리스트 형식으로 들고온다.
		/* total_revenue 만 싹 더한다. 이 값이 세틀먼트 테이블의 total_revenue 가 된다.
		 *  이 때 스토어 아이디별로 따로 더해야한다.
		 *  */
		// settlement_amount = (세틀먼트 테이블의 total_revenue) - (total 수수료)

		int prevMonth = currentMonth - 1;

		// 스토어별, 월별 총 매출액 리스트
		List<Object[]> monthlyStoreRevenue = revenueRepository.findMonthlyTotalRevenue(prevMonth);

		List<StoreSettlement> settlementList = new ArrayList<>();

		for (Object[] row : monthlyStoreRevenue) {
			int storeId = (int)row[0];
			long totalRevenue = (long)row[1];

			// 플랫폼 수수료 기본 10%로 계산 (수수료 산정 방식 지정 필요)
			FeeRate feeRate = FeeRate.MEDIUM;
			long platformFeeRate = (long)(feeRate.getFeeRate() * totalRevenue);

			// 정산 금액
			long settlementAmount = totalRevenue - platformFeeRate;

			StoreSettlement newStoreSettlement = StoreSettlement.builder()
				.storeId(storeId)
				.totalRevenue(totalRevenue)
				.settlementAmount(settlementAmount)
				.platformFeeRate(feeRate)
				.build();

			settlementList.add(newStoreSettlement);
		}

		settlementRepository.saveAll(settlementList);
	}
}
