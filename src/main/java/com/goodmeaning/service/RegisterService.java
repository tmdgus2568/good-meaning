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
	
	// 아이디 중복 체크 (true: 이미 있음, false: 없음 )
	public boolean checkUserId(String userId) {
		boolean result = false;
		if(userRepo.findByUserId(userId).isPresent()) result = true;
		return result;
	}
}
