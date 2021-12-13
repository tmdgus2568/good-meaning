package com.goodmeaning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class MypageService {
	
	@Autowired
	UserRepository userRepo;

	public void update(UserVO user) {
		userRepo.save(user);
		
	}

}
