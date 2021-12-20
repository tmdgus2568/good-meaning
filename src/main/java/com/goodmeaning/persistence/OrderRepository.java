package com.goodmeaning.persistence;

import java.util.List;


import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.UserVO;


public interface OrderRepository extends CrudRepository<OrderVO, Long> {
	List<OrderVO> findByUserPhone(UserVO user);
}
