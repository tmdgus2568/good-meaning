package com.goodmeaning.persistence;

import java.util.List;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;


public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetailVO, Long>, QuerydslPredicateExecutor<OrderDetailVO>{
	List<OrderDetailVO> findByOrderNum(OrderVO orderNum);
	OrderDetailVO findByOrderNumAndProductNum(OrderVO orderNum, ProductVO productNum);
	
	int countByProductNum(ProductVO productNum);
}
