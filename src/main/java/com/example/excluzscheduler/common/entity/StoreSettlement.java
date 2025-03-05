package com.example.excluzscheduler.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.excluzscheduler.domain.store.storeSettlement.enums.FeeRate;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.SettlementStatus;
import com.example.excluzscheduler.domain.store.storeSettlement.enums.SettlementPeriod;

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
public class StoreSettlement extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "store_id")
	private Integer storeId;

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

	@Column(name = "settlement_period", nullable = false)
	@Enumerated(EnumType.STRING)
	private SettlementPeriod settlementPeriod; // 정산 주기

	// 정산 기간(범위): startDate ~ endDate
	@Column(name = "start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = false)
	private LocalDateTime endDate;

	@Builder
	public StoreSettlement(
		Integer storeId,
		Long totalRevenue,
		FeeRate platformFeeRate,
		Long settlementAmount,
		SettlementPeriod settlementPeriod,
		LocalDateTime startDate,
		LocalDateTime endDate
	) {
		this.storeId=storeId;
		this.totalRevenue=totalRevenue;
		this.platformFeeRate= platformFeeRate;
		this.settlementAmount=settlementAmount;
		this.settlementStatus=SettlementStatus.WAITING;
		this.settlementPeriod=settlementPeriod;
		this.startDate=startDate;
		this.endDate=endDate;
	}

	public void updateStoreSettlement(
		Long totalRevenue,
		FeeRate platformFeeRate,
		Long settlementAmount
	) {
		if (totalRevenue != null) this.totalRevenue = totalRevenue;
		if (platformFeeRate != null) this.platformFeeRate = platformFeeRate;
		if (settlementAmount != null) this.settlementAmount = settlementAmount;
	}
}
