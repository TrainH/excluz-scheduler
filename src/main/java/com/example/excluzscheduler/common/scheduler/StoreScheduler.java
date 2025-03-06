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

    private void executeScheduler(RevenuePeriod revenuePeriod) {
        LocalDateTime nowDatetime = LocalDateTime.now();
        LocalDateTime startDatetime = revenuePeriod.getStartDatetime(nowDatetime);
        LocalDateTime endDatetime = revenuePeriod.getEndDatetime(nowDatetime);

        log.info("🖍️ {} 스케줄러 시작", revenuePeriod);

        storeRevenueService.createRevenue(revenuePeriod, startDatetime, endDatetime);
        storeRankingService.updateStoreRankings(revenuePeriod, startDatetime, endDatetime);

        if (revenuePeriod == RevenuePeriod.MONTH) {
            storeSettlementService.createSettlement(revenuePeriod, startDatetime, endDatetime);
        }

        log.info("🟢 {} 스케줄러 끝", revenuePeriod);
    }

    // 실시간
    @Scheduled(fixedDelay = 60000)
    public void scheduleRealTimeRevenueUpdate() {

        LocalDateTime nowDatetime = LocalDateTime.now();
        RevenuePeriod revenuePeriod = RevenuePeriod.DAY; // 테스트할 기간 선택

        LocalDateTime startDatetime = revenuePeriod.getStartDatetime(nowDatetime).plusDays(1); // 당일
        LocalDateTime endDatetime = revenuePeriod.getEndDatetime(nowDatetime).plusDays(1); // 내일

        log.info("🖍️ 실시간 스케줄러 시작");

        storeRevenueService.createRevenue(revenuePeriod, startDatetime, endDatetime);
        storeRankingService.updateStoreRankings(revenuePeriod, startDatetime, endDatetime);

        log.info("🟢 실시간 스케줄러 끝");
    }

    // 일간 - 사용자가 사용하지 않는 시간에 돌도록 하기
    @Scheduled(cron = "0 0 3 * * *") // 매 새벽 3시
    public void scheduleDailyRevenueUpdate() {
        executeScheduler(RevenuePeriod.DAY);
    }

    // 월간
    @Scheduled(cron = "0 0 3 1 * *") // 매월 1일 새벽 3시
    public void scheduleMonthlyRevenueUpdate() {
        executeScheduler(RevenuePeriod.MONTH);
    }

    // 연간
    @Scheduled(cron = "0 0 3 1 1 *") // 매년 1월 1일 새벽 3시
    public void scheduleYearlyRevenueUpdate() {
        executeScheduler(RevenuePeriod.YEAR);
    }
}