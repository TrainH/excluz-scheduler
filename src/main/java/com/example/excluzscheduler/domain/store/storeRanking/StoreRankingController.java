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

	// TOP 10 랭킹 조회
	@GetMapping("/top10")
	public List<StoreRankingTop10ResponseDto> getTop10StoreRankings() {
		return storeRankingService.getTop10StoreRankings();
	}
}
