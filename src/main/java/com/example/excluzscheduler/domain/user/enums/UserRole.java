package com.example.excluzscheduler.domain.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {
	ADMIN("ROLE_ADMIN", "관리자"), // 관리자
	CUSTOMER("ROLE_CUSTOMER", "일반 회원"), // 일반 회원
	STREAMER("ROLE_STREAMER", "굿즈 판매자") // 굿즈 판매자
	;
	private final String role;
	private final String description;

	UserRole(String role, String description) {
		this.role = role;
		this.description = description;
	}
}
