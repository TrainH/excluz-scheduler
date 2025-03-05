package com.example.excluzscheduler.common.entity;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Table(name = "store_revenues")
@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StoreRevenue {

    @EmbeddedId
    private StoreRevenueId id;

    @Column(name = "total_revenue", nullable = false)
    private Long totalRevenue;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public StoreRevenue(
            Integer storeId,
            RevenuePeriod revenuePeriod,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            Long totalRevenue
    ) {
        this.id = new StoreRevenueId(storeId, revenuePeriod, startDatetime, endDatetime);
        this.totalRevenue = totalRevenue;
    }

    public Integer getStoreId() {
        return id.getStoreId();
    }

    public RevenuePeriod getRevenuePeriod() {
        return id.getRevenuePeriod();
    }

    public LocalDateTime getStartDatetime() {
        return id.getStartDatetime();
    }

    public LocalDateTime getEndDatetime() {
        return id.getEndDatetime();
    }
}