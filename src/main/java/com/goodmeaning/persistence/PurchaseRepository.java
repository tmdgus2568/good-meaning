package com.goodmeaning.persistence;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goodmeaning.vo.PurchaseVO;


public interface PurchaseRepository extends PagingAndSortingRepository<PurchaseVO, Long>, QuerydslPredicateExecutor<PurchaseVO> {
	
}
