package com.example.excluzscheduler.domain.store.storeRevenue.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.excluzscheduler.common.entity.StoreRevenue;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRevenueRepository extends JpaRepository<StoreRevenue, Integer> {

	// 매출 기준으로 스토어 조회
	@Query("SELECT sr FROM StoreRevenue sr WHERE sr.id.startDatetime >= :startDate AND sr.id.endDatetime < :endDate ORDER BY sr.totalRevenue DESC")
	List<StoreRevenue> findStoreRevenuesByPeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query("SELECT sr.id.storeId, SUM(sr.totalRevenue) " +
	"FROM StoreRevenue sr " +
	"WHERE (sr.id.startDatetime = :startDateTime) " +
	"AND (sr.id.endDatetime = :endDateTime) " +
	"AND sr.id.revenuePeriod = :period " +
	"GROUP BY sr.id.storeId")
	List<Object[]> findTotalRevenueWithPeriod(
		@Param("startDateTime") LocalDateTime startDateTime,
		@Param("endDateTime") LocalDateTime endDateTime,
		@Param("period") RevenuePeriod period);
}