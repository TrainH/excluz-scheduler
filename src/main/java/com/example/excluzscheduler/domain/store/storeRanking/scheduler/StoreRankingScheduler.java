package com.example.excluzscheduler.domain.store.storeRanking.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// @Profile("batch") // <- batch 말고 다른 이름으로 명명하고 싶으면 바꿔도 됨
// spring.profiles.active 값이 batch 일 때만 지금 StoreRankingScheduler Bean 이 생성되고, 그 때문에 spring.profiles.active 값이 batch 가 아닌 경우에는 해당 Bean 이 생성이 안되니까 스케쥴링해둔 게 안돈다
// 로컬에서 스케쥴러를 테스트할때에는 Intellij 에서 Edit Configurations 를 눌러서 activeProfiles 라는 곳에 batch 를 넣어두고 테스트하면 된다.

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreRankingScheduler { // todo 클래스 이름 바꾸기! 서비스로! (함수를 서비스로 넘기기)

	private final StoreRankingService storeRankingService;

	// 매일 자정마다 실행되는 스케줄러 (TOP 10 랭킹 업데이트)
	public void updateDailyStoreRankings() {
		log.info("🟢 일간 랭킹 업데이트 시작");

		RevenuePeriod rankingPeriod = RevenuePeriod.D; // 일간 매출 기준 랭킹
		LocalDateTime startDate = LocalDate.now().minusDays(1).atStartOfDay();
		LocalDateTime endDate = LocalDate.now().atStartOfDay();

		storeRankingService.createDailyRanking(rankingPeriod, startDate, endDate);

		log.info("✅ 일간 랭킹 업데이트 완료!");
	}
}
