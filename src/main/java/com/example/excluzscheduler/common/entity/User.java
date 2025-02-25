package com.example.excluzscheduler.common.entity;

import com.example.excluzscheduler.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Comment("유저 실명")
	@Column(length = 30, nullable = false)
	private String name;

	@Comment("유저 별명")
	@Column(name = "nick_name", length = 10, unique = true, nullable = false) // 중복 닉네임 X
	private String nickName;

	@Comment("유저 전화번호")
	@Column(name = "phone_number",columnDefinition = "CHAR(15)", unique = true, nullable = false) // 중복 전화번호 X
	private String phoneNumber;

	@Comment("유저 집주소")
	@Column(length = 100, nullable = false)
	private String address;

	@Comment("유저 이메일")
	@Column(length = 50, unique = true, nullable = false)
	private String email;

	@Comment("유저 비밀번호")
	@Column(length = 60, nullable = false)
	private String password;

	@Comment("유저 타입")
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Comment("유저 탈퇴 여부")
	@Column(name = "is_deleted", columnDefinition = "TINYINT")
	private Boolean isDeleted;
}
