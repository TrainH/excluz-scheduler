package com.example.excluzscheduler.common.entity;

import com.example.excluzscheduler.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "streamers")
@NoArgsConstructor
public class Streamer extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(name = "nick_name", nullable = false, unique = true, length = 10)
	private String nickName;

	@Column(name = "phone_number", nullable = false, unique = true, columnDefinition = "char(15)")
	private String phoneNumber;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Column(nullable = false, length = 60)
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	@Column(name = "is_deleted")
	private boolean isDeleted;
}
