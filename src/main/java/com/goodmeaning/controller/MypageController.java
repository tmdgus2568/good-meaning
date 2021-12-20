package com.goodmeaning.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goodmeaning.service.MypageService;
import com.goodmeaning.vo.UserVO;

@Controller
public class MypageController {
	@Autowired
	MypageService mypageService;
	
	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.GET)
	public String updateUserForm(HttpSession session) {
		
		return "user/mypage/user_update";
	}

	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.POST)
	public String updateUser(UserVO user) {
		System.out.println(user);
		mypageService.update(user);
		return "user/mypage/user_update";
	}

}
