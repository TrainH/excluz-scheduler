package com.example.excluzscheduler.common.scheduler;

import com.example.excluzscheduler.domain.store.storeRevenue.scheduler.StoreRevenueScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreScheduler {

    private final StoreRevenueScheduler storeRevenueScheduler;

    // 매일 00:00:00 (자정) 실행 ("0 0 0 * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleStore() {
        log.info("start store scheduler");
        storeRevenueScheduler.createDailyRevenue();

        log.info("finish store scheduler");
    }
}