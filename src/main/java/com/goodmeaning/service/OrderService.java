package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.CartRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class OrderService {
	@Autowired
	UserRepository urepo;
	
	@Autowired
	CartRepository crepo;
	
	@Autowired
	OrderRepository orepo;
	
	public void userCheck(UserVO user) {
		urepo.save(user);
	}
	
	//장바구니->구매하기
	public List<Object[]> findOrderList(Long[] cartNum) {
		return crepo.findOrderList(cartNum);
	}
	
}
