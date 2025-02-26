package com.example.excluzscheduler.domain.store.storeSettlement.scheduler;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.excluzscheduler.domain.store.storeSettlement.service.StoreSettlementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreSettlementScheduler {

	private final StoreSettlementService settlementService;

	public void createSettlement() {
		log.info("Create settlement");

		int currentMonth = LocalDate.now().getDayOfMonth();
		settlementService.createMonthlySettlement(currentMonth); // 월별 정산

		log.info("Finish settlement");
	}
}
