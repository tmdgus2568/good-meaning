package com.goodmeaning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.goodmeaning.service.RegisterService;
import com.goodmeaning.vo.UserVO;

@Controller
public class RegisterController {
	@Autowired
	RegisterService registerService;
	
	// 회원가입 - 회원가입창 
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerForm() {
		return "user/register/register";
	}
	
	// 회원가입 - 회원가입 완료
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(UserVO user) {
		System.out.println(user);
		registerService.register(user);
		return "user/register/registerConfirm";
	}
	

	
	

}
