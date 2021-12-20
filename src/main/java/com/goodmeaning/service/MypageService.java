package com.goodmeaning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.UserVO;

@Service
public class MypageService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	OrderDetailRepository orderDetailRepo;

	public void update(UserVO user) {
		userRepo.save(user);
		
	}
	
	public List<OrderVO> orders(UserVO user) {
		return orderRepo.findByUserPhone(user);
	}
	
	// orderNum을 가지고 그 orderNum을 가지는 Detail들을 가져온다 
	public List<OrderDetailVO> orderDetails(Long orderNum){
		Optional<OrderVO> order = orderRepo.findById(orderNum);
		List<OrderDetailVO> details = null;
		if(order.isPresent()) {
			details = orderDetailRepo.findByOrderNum(order.get());
		}
		return details;
		
	}

}
