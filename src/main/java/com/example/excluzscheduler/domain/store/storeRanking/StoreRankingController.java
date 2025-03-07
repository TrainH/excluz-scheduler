package com.example.excluzscheduler.domain.store.storeRanking;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto;
import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/store-ranking")
@RequiredArgsConstructor
public class StoreRankingController {
	private final StoreRankingService storeRankingService;

	// TOP 10 랭킹 조회 (매출 정보 제외)
	// RequestParam value의 enum(DAY, MONTH, YEAR)에 따라 동적으로 조회 가능
	@GetMapping("/top10")
	public List<StoreRankingTop10ResponseDto> getTop10StoreRankings(
		@RequestParam(value = "period", defaultValue = "DAY") String period
	) {
		RevenuePeriod revenuePeriod = RevenuePeriod.valueOf(period.toUpperCase());
		return storeRankingService.getTop10StoreRankings(revenuePeriod);
	}
}
