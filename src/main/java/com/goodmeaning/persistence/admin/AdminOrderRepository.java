package com.goodmeaning.persistence.admin;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goodmeaning.vo.OrderVO;


public interface AdminOrderRepository extends PagingAndSortingRepository<OrderVO, Long> ,QuerydslPredicateExecutor<OrderVO> {

	
	
}
