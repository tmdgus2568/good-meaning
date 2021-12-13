package com.goodmeaning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class RegisterService {
	@Autowired
	UserRepository userRepo;
	
	// 회원가입 
	public void register(UserVO user) {
		userRepo.save(user);
	}
}
