package com.example.excluzscheduler.common.entity;

import com.example.excluzscheduler.domain.store.storeRevenue.enums.RevenuePeriod;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRevenueId implements Serializable {

    private Integer storeId;

    @Enumerated(EnumType.STRING)
    private RevenuePeriod revenuePeriod;

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
}