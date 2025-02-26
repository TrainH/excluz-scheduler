package com.example.excluzscheduler.domain.store.storeSettlement.enums;

import lombok.Getter;

@Getter
public enum SettlementStatus {
	PENDING("출금 요청 전"),
	PROCESSING("스트리머 출금 요청"),
	COMPLETED("출금 완료");

	private final String description;

	SettlementStatus(String description) {
		this.description = description;
	}
}
