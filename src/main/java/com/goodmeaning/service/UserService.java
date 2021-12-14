package com.goodmeaning.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

// 회원 관리 
// 회원가입, 로그인, 회원탈퇴 
@Service
public class UserService {
	
	@Autowired
	UserRepository userRepo;
	
	// 회원가입 
	public void register(UserVO user) {
		userRepo.save(user);
	}
	
	// 로그인
	public Optional<UserVO> checkLogin(String id, String pw) {
		return userRepo.findByUserIdAndUserPw(id, pw);
	}


}
