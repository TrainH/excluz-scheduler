package com.example.excluzscheduler.domain.store.storeRanking;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto;
import com.example.excluzscheduler.domain.store.storeRanking.service.StoreRankingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/store-ranking")
@RequiredArgsConstructor
public class StoreRankingController {
	private final StoreRankingService storeRankingService;

	// 매일 자정마다 실행되는 스케줄러 (TOP 10 랭킹 업데이트)
	@GetMapping("/top10")
	public List<StoreRankingTop10ResponseDto> getTop10StoreRankings() {
		return storeRankingService.getTop10StoreRankings();
	}
}
