package com.example.excluzscheduler.domain.store.storeRevenue.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreRevenueScheduler {

//    private final StoreRevenueService storeRevenueService;
//
//    // 매일 자정(00:00:00)에 실행
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void collectDailyRevenue() {
//        log.info("📊 스토어 매출 데이터 수집 시작...");
//
//        storeRevenueService.saveRevenue(revenue);
//        log.info("✅ 매출 데이터 저장 완료: {}", revenue);
//    }
}
