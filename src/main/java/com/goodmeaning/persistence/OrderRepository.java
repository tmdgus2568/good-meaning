package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.OrderVO;

public interface OrderRepository extends CrudRepository<OrderVO, Long> {

}
