package com.goodmeaning.controller;

import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goodmeaning.service.ManageUserService;
import com.goodmeaning.vo.UserVO;


//회원 관리 
//회원가입, 로그인, 회원탈퇴 


@Controller
public class ManageUserController {
	
	@Autowired
	ManageUserService manageUserService;
	
	// 회원가입 - 회원가입창 
	@RequestMapping(value = "/auth/signUp", method = RequestMethod.GET)
	public String signUpForm() {
		return "user/auth/signUp";
	}
	
	// 회원가입 - 회원가입 완료
	@RequestMapping(value = "/auth/signUp", method = RequestMethod.POST)
	public String signUp(UserVO user) {
		System.out.println(user);
		manageUserService.singUp(user);
		return "user/auth/signUpConfirm";
	}
	
	
	// 로그인 - 로그인 창
	@RequestMapping(value = "/auth/login", method = RequestMethod.GET)
	public String loginForm() {
		return "user/auth/login";
	}
	
	// 로그인 - 로그인 실행 
	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	public String login(String userId, String userPw, HttpSession session) {
		// 로그인 성공시 
		Optional<UserVO> user = manageUserService.checkLogin(userId, userPw);
		System.out.println(user);
		if(user.isPresent()) {
			session.setAttribute("user", (UserVO)user.get());
			return "redirect:/";
		}
		return "user/auth/login";
		
	}

}
