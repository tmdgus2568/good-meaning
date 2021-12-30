package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.CartRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class OrderService {
	@Autowired
	UserRepository urepo;
	
	@Autowired
	CartRepository crepo;
	
	public void userCheck(UserVO user) {
		urepo.save(user);
	}

	public List<Object[]> findOrderList(Long[] cartNum) {
		// TODO Auto-generated method stub
		 
		return crepo.findOrderList2(cartNum);
	}
}
