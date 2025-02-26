package com.example.excluzscheduler.domain.store.storeRevenue.repository;

import java.util.List;

import com.example.excluzscheduler.common.entity.StoreRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRevenueRepository extends JpaRepository<StoreRevenue, Integer> {

	@Query("SELECT sr.storeId, SUM(sr.totalRevenue) " +
	"FROM StoreRevenue sr " +
	"WHERE FUNCTION('MONTH', sr.startDate) = :prevMonth " +
	"GROUP BY sr.storeId")
	List<Object[]> findMonthlyTotalRevenue(@Param("prevMonth") int prevMonth);
}
