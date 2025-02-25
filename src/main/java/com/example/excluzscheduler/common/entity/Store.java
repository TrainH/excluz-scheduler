package com.example.excluzscheduler.common.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "stores")
@NoArgsConstructor
public class Store extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "streamer_id")
	private Streamer streamer;

	@Column(length = 100, nullable = false)
	private String address;

	@Column(length = 30, unique = true, nullable = false)
	private String storeName;

	@Column(length = 30, nullable = false)
	private String registrationNumber;

	@Column(name = "is_deleted")
	private boolean isDeleted;
}
