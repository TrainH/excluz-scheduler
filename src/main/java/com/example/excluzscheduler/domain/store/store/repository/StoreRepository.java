package com.example.excluzscheduler.domain.store.store.repository;

import java.util.List;

import com.example.excluzscheduler.common.entity.Store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Integer> {

	// 여러 개의 Store ID를 한 번에 조회하는 메서드 추가 (N+1 문제 해결)
	List<Store> findByIdIn(List<Integer> ids);

}