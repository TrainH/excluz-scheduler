package com.example.excluzscheduler.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.excluzscheduler.domain.store.storeSettlement.enums.FeeRate;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.SettlementStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "store_settlement")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class StoreSettlement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(name = "settlements_period")
	private LocalDate settlementPeriod;

	@Column(name = "total_revenue")
	private Long totalRevenue;

	@Column(name = "platform_fee_rate")
	@Enumerated(EnumType.STRING)
	private FeeRate platformFeeRate;

	@Column(name = "settlement_amount")
	private Long settlementAmount;

	@Column(name = "settlement_status")
	@Enumerated(EnumType.STRING)
	private SettlementStatus settlementStatus;

	@LastModifiedDate
	@Column(name = "updated_at", nullable = false, updatable = false)
	private LocalDateTime updatedAt;
}
