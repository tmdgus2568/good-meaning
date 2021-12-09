package com.goodmeaning.controller;

import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goodmeaning.service.UserService;
import com.goodmeaning.vo.UserVO;


//회원 관리 
//회원가입, 로그인, 회원탈퇴 , 회원수정 


@Controller
public class UserController {
	
	@Autowired
	UserService manageUserService;
	
	// 회원가입 - 회원가입창 
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerForm() {
		return "user/auth/register";
	}
	
	// 회원가입 - 회원가입 완료
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(UserVO user) {
		System.out.println(user);
		manageUserService.register(user);
		return "user/auth/registerConfirm";
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
	
	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.GET)
	public String updateUserForm() {

		return "user/mypage/userUpdate";
	}

	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.POST)
	public String updateUser(UserVO user) {
		System.out.println(user);
		manageUserService.register(user);
		return "user/mypage/userUpdate";
	}
	
}
