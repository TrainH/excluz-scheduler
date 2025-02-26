package com.example.excluzscheduler.domain.store.storeRanking.scheduler;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;

import lombok.RequiredArgsConstructor;

// @Profile("batch") // <- batch 말고 다른 이름으로 명명하고 싶으면 바꿔도 됨
// spring.profiles.active 값이 batch 일 때만 지금 StoreRankingScheduler Bean 이 생성되고, 그 때문에 spring.profiles.active 값이 batch 가 아닌 경우에는 해당 Bean 이 생성이 안되니까 스케쥴링해둔 게 안돈다
// 로컬에서 스케쥴러를 테스트할때에는 Intellij 에서 Edit Configurations 를 눌러서 activeProfiles 라는 곳에 batch 를 넣어두고 테스트하면 된다.
@Component
@RequiredArgsConstructor
public class StoreRankingScheduler {
	private final StoreRankingService storeRankingService;

	// 공식 랭킹 TOP 10: 인증 X (누구나 확인 가능)
	// @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Scheduled(cron = "0 */1 * * * *") // todo 현재 테스트 위해 매분 실행으로 세팅함
	public void updateDailyStoreRankings() {
		storeRankingService.updateDailyRankings();
	}



}
