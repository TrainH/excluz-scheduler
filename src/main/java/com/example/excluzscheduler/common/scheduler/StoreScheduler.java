package com.example.excluzscheduler.common.scheduler;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;
import com.example.excluzscheduler.domain.store.storeRevenue.service.StoreRevenueService;
import com.example.excluzscheduler.domain.store.storeSettlement.service.StoreSettlementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreScheduler {


    private final StoreRevenueService storeRevenueService;
    private final StoreRankingService storeRankingService;
    private final StoreSettlementService storeSettlementService;

    @Scheduled(fixedDelay = 30000) // 로직 작업 끝나고 30초 뒤 로직 돌아감

    public void scheduleStore() {

        LocalDateTime nowDatetime = LocalDateTime.now();
        RevenuePeriod revenuePeriod = RevenuePeriod.MINUTE_1; // 테스트할 기간 선택

        LocalDateTime startDatetime = revenuePeriod.getStartDatetime(nowDatetime);
        LocalDateTime endDatetime = revenuePeriod.getEndDatetime(nowDatetime);

        log.info("start store scheduler");

        storeRevenueService.createRevenue(revenuePeriod, startDatetime, endDatetime);
        storeRankingService.updateDailyStoreRankings(revenuePeriod, startDatetime, endDatetime);
        storeSettlementService.createSettlement(revenuePeriod, startDatetime, endDatetime); // TODO 테스트 이후 월별 정산으로 매개변수 변경 필요

        log.info("finish store scheduler");
    }

}