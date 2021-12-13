package com.goodmeaning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class LoginService {
	@Autowired
	UserRepository userRepo;
	
	// 로그인
	public Optional<UserVO> checkLogin(String id, String pw) {
		return userRepo.findByUserIdAndUserPw(id, pw);
	}


}
