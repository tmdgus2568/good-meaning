package com.goodmeaning.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goodmeaning.service.LoginService;
import com.goodmeaning.vo.UserVO;

@Controller
public class LoginController {
	@Autowired
	LoginService loginService;
	
	@RequestMapping(value = "/auth/loginKakao", method = RequestMethod.GET)
	public String loginKaKao(@RequestParam("code") String code) {
	    System.out.println("code : " + code);
	    return "/user/test";
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
		Optional<UserVO> user = loginService.checkLogin(userId, userPw);
		System.out.println(user);
		if(user.isPresent()) {
			session.setAttribute("user", (UserVO)user.get());
			return "redirect:/";
		}
		return "user/auth/login";
		
	}
}
