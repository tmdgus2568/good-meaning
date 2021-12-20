package com.goodmeaning.persistence;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;


public interface OrderDetailRepository extends CrudRepository<OrderDetailVO, Long>{
	List<OrderDetailVO> findByOrderNum(OrderVO orderNum);
}
