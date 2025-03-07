package com.example.excluzscheduler.domain.store.storeRanking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.excluzscheduler.common.entity.StoreRanking;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

@Repository
public interface StoreRankingRepository extends JpaRepository<StoreRanking, Long> {
	// 특정 랭킹 기간에 대한 전체 랭킹 조회
	@Query("SELECT sr FROM StoreRanking sr WHERE sr.rankingPeriod = :period ORDER BY sr.rankPosition ASC")
	Page<StoreRanking> findAllByRankingPeriod(@Param("period") RevenuePeriod period, Pageable pageable);
}