package com.example.excluzscheduler.domain.store.storeSettlement.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.excluzscheduler.common.entity.StoreSettlement;
import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;

public interface StoreSettlementRepository extends JpaRepository<StoreSettlement, Integer> {

	@Query("SELECT s FROM StoreSettlement s " +
	"WHERE s.startDate = :startDateTime " +
	"AND s.endDate = :endDateTime " +
	"AND s.settlementPeriod = :period ")
	List<StoreSettlement> findByPeriod(
		@Param("startDateTime") LocalDateTime startDateTime,
		@Param("endDateTime") LocalDateTime endDateTime,
		@Param("period") RevenuePeriod period
	);
}
