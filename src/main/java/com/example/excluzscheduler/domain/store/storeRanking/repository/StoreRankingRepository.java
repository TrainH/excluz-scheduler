package com.example.excluzscheduler.domain.store.storeRanking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.excluzscheduler.common.entity.Store;
import com.example.excluzscheduler.common.entity.StoreRanking;
import com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

@Repository
public interface StoreRankingRepository extends JpaRepository<StoreRanking, Long> {

	// 일간 TOP 10 조회 (JPA Pageable 사용)
	@Query("SELECT new com.example.excluzscheduler.domain.store.storeRanking.dto.response.StoreRankingTop10ResponseDto(s.id, s.name, " +
		"RANK() OVER (ORDER BY SUM(sr.revenue) DESC)) " +
		"FROM StoreRevenue sr " +
		"JOIN sr.store s " +
		"WHERE sr.period = :period " +
		"AND sr.startDate <= CURRENT_DATE AND sr.endDate >= CURRENT_DATE " +
		"GROUP BY s.id, s.name " +
		"ORDER BY SUM(sr.revenue) DESC")
	List<StoreRankingTop10ResponseDto> findTop10StoresForPublic(@Param("period") RevenuePeriod period, Pageable pageable);

	// 기존 랭킹 존재 여부 확인 (업데이트를 위해)
	Optional<StoreRanking> findByStoreAndPeriod(Store store, RevenuePeriod period);
}