package com.example.excluzscheduler.domain.point.pointTransaction.repository;

import com.example.excluzscheduler.common.entity.PointTransaction;
import com.example.excluzscheduler.domain.point.pointTransaction.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Integer> {
    @Query("SELECT pt FROM PointTransaction pt " +
            "JOIN FETCH pt.order " +
            "JOIN FETCH pt.user " +
            "JOIN FETCH pt.store " +
            "WHERE :startDatetime <= pt.createdAt AND pt.createdAt < :endDatetime " +
            "AND pt.transactionType IN (:transactionTypeList)")
    List<PointTransaction> findAllByDateRangeAndTransactionType(
            @Param("startDatetime") LocalDateTime startDatetime,
            @Param("endDatetime") LocalDateTime endDatetime,
            @Param("transactionTypeList") List<TransactionType> transactionTypeList);
}
