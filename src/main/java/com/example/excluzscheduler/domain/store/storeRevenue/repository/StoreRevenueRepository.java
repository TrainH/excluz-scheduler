package com.example.excluzscheduler.domain.store.storeRevenue.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.excluzscheduler.common.entity.StoreRevenue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRevenueRepository extends JpaRepository<StoreRevenue, Integer> {

	// 매출 기준으로 스토어 조회
	@Query("SELECT sr FROM StoreRevenue sr WHERE sr.startDate >= :startDate AND sr.endDate < :endDate ORDER BY sr.totalRevenue DESC")
	List<StoreRevenue> findStoresByRevenue(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}