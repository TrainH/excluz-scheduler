package com.example.excluzscheduler.common.scheduler;

import com.example.excluzscheduler.domain.store.storeRevenue.scheduler.StoreRevenueScheduler;
import com.example.excluzscheduler.domain.store.storeSettlement.scheduler.StoreSettlementScheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreScheduler {

    private final StoreRevenueScheduler storeRevenueScheduler;
    private final StoreSettlementScheduler storeSettlementScheduler;

    // 매일 00:00:00 (자정) 실행 ("0 0 0 * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleStore() {
        log.info("start store scheduler");

        storeRevenueScheduler.createDailyRevenue();

        log.info("finish store scheduler");
    }

    // 매달 1일 자정 실행
    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduleStoreMonthly() {
        log.info("start store scheduler monthly");

        storeSettlementScheduler.createSettlement();

        log.info("finish store scheduler monthly");
    }
}