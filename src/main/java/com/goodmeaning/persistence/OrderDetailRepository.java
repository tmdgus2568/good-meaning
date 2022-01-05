package com.goodmeaning.persistence;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;


public interface OrderDetailRepository extends CrudRepository<OrderDetailVO, Long>{
	List<OrderDetailVO> findByOrderNum(OrderVO orderNum);
	OrderDetailVO findByOrderNumAndProductNum(OrderVO orderNum, ProductVO productNum);
	
	int countByProductNum(ProductVO productNum);
}
