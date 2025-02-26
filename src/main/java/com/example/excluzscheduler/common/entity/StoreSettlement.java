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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
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

	@Column(name = "store_id")
	private Integer storeId;

	@Column(name = "settlements_period", nullable = false)
	private LocalDate settlementPeriod; // 정산 내역 생성일

	@Column(name = "total_revenue", nullable = false)
	private Long totalRevenue;

	@Column(name = "platform_fee_rate", nullable = false)
	@Enumerated(EnumType.STRING)
	private FeeRate platformFeeRate;

	@Column(name = "settlement_amount", nullable = false)
	private Long settlementAmount;

	@Column(name = "settlement_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private SettlementStatus settlementStatus; // 정산 상태

	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Builder
	public StoreSettlement(
		Integer storeId,
		Long totalRevenue,
		FeeRate platformFeeRate,
		Long settlementAmount
	) {
		this.storeId=storeId;
		this.settlementPeriod=LocalDate.now();
		this.totalRevenue=totalRevenue;
		this.platformFeeRate= platformFeeRate;
		this.settlementAmount=settlementAmount;
		this.settlementStatus=SettlementStatus.PENDING;
		this.updatedAt=LocalDateTime.now();
	}
}
