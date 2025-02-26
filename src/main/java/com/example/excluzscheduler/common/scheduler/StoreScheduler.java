package com.example.excluzscheduler.common.scheduler;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRevenue.service.StoreRevenueService;
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

    // 매분 0초에 실행
    @Scheduled(cron = "0 * * * * ?")
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

        log.info("finish store scheduler");
    }

}