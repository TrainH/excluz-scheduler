package com.example.excluzscheduler.common.scheduler;


import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRanking.scheduler.StoreRankingScheduler;
import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;
import com.example.excluzscheduler.domain.store.storeRevenue.scheduler.StoreRevenueScheduler;
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

    // 매일 00:00:00 (자정) 실행 ("0 0 0 * * ?")
    // @Scheduled(cron = "0 0 0 * * ?")
    // todo 동작 완료 후 특정한 시간 지난 이후에 실행되도록 하기
    @Scheduled(cron = "0 */1 * * * *") // todo 현재 테스트 위해 매분 실행으로 세팅함 (cron으로 시간 안 정하고 할 수 있음 -> fixedRate, fixedDelay 공부해서 적용하기)
    public void scheduleStore() {

        LocalDateTime nowDatetime = LocalDateTime.now();
        RevenuePeriod revenuePeriod = RevenuePeriod.HOUR_1; // 테스트할 기간 선택

        LocalDateTime startDatetime = revenuePeriod.getStartDatetime(nowDatetime);
        LocalDateTime endDatetime = revenuePeriod.getEndDatetime(nowDatetime);

//        System.out.println("nowDatetime: " + nowDatetime);
//        System.out.println("revenuePeriod: " + revenuePeriod);
//        System.out.println("startDatetime: " + startDatetime);
//        System.out.println("endDatetime: " + endDatetime);

        log.info("start store scheduler");

        storeRevenueService.createRevenue(revenuePeriod, startDatetime, endDatetime);
        storeRankingService.updateDailyStoreRankings();

        log.info("finish store scheduler");
    }

}