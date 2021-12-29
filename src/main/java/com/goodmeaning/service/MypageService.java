package com.goodmeaning.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserVO;
import com.querydsl.core.types.Predicate;

@Service
public class MypageService {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	OrderDetailRepository orderDetailRepo;

	@Autowired
	ReviewRepository reviewRepo;
	
	public Optional<UserVO> findUser(String userPhone) {
		return userRepo.findById(userPhone);
	}
	
	public void updateUser(UserVO user) {
		userRepo.save(user);
		
	}
	
	public List<OrderVO> orders(UserVO user) {
		return orderRepo.findByUserPhone(user);
	}
	
	public Optional<OrderVO> findOrderById(long oid){
		return orderRepo.findById(oid);
	}
	
	// 구매취소
	@Transactional
	public void updateOrder(OrderVO order) {
		orderRepo.save(order);
		for(OrderDetailVO detail:order.getDetails()) {
			// productNum의 정보를 가져온 뒤, stock+1 후 다시 저장 
			Optional<ProductVO> detailInfo = productRepo.findById(detail.getProductNum().getProductNum());
			if(detailInfo.isPresent()) {
				detailInfo.get().setProductStock(detailInfo.get().getProductStock()+1);
				productRepo.save(detailInfo.get());
			}
		}
		
	}
	
	// reviews 가져오기
	public List<ReviewVO> reviews(UserVO user){
		return reviewRepo.findByUserPhone(user);
	}
	
	// review 가져오기
	public Optional<ReviewVO> review(long no){
		return reviewRepo.findById(no);
	}
	

}
