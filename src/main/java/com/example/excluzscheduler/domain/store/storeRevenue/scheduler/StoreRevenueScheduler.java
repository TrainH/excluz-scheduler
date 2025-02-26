package com.example.excluzscheduler.domain.store.storeRevenue.scheduler;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import com.example.excluzscheduler.domain.store.storeRevenue.service.StoreRevenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreRevenueScheduler {

    private final StoreRevenueService storeRevenueService;

    public void createDailyRevenue() {

        log.info("Create daily revenue");

        RevenuePeriod revenuePeriod = RevenuePeriod.D;
        LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atStartOfDay();

        storeRevenueService.createRevenue(revenuePeriod, startDate, endDate);

        log.info("Finish daily revenue");

    }
}
