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
	
	// 카카오 로그인 확인(id 확인)
	public Optional<UserVO> checkSocialLogin(String id){
		return userRepo.findByUserId(id);
	}

	// 로그인2
	public Optional<UserVO> findByUserId(String id) {
		return userRepo.findByUserId(id);
	}

}
