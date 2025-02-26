package com.example.excluzscheduler.common.entity;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "store_revenues")
@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class StoreRevenue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(name = "total_revenue", nullable = false)
    private Long totalRevenue;

    @Enumerated(EnumType.STRING)
    @Column(name = "revenue_period", nullable = false)
    private RevenuePeriod revenuePeriod;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public StoreRevenue(
            Integer storeId,
            Long totalRevenue,
            RevenuePeriod revenuePeriod,
            LocalDate startDate,
            LocalDate endDate
    ) {
        this.storeId = storeId;
        this.totalRevenue = totalRevenue;
        this.revenuePeriod = revenuePeriod;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}