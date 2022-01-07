package com.goodmeaning.controller.admin;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodmeaning.service.admin.AdminLoginService;
import com.goodmeaning.vo.UserVO;

@Controller
public class AdminLoginController {
	
	//브라우저 해석 : static //서버해석 : templates
	
	@Autowired
	AdminLoginService loginService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//로그인 페이지 
	@GetMapping ("/adminlogin") //주소창에서 들어오는것! 
	public String getLoginPage() {
		return "admin/auth/login";
	}
	
	//요청방식이 다르기때문에 맵핑주소 같아도 됨
	//로그인 실행
	@PostMapping("/admin/adminlogin")
	public String login(String username, String password, HttpSession session) { //String userRole, 
		// 로그인 성공시 
		Optional<UserVO> user = loginService.checkLogin(username, passwordEncoder.encode(password));
		System.out.println("user : " + user);
		if(user.isPresent()) {
			session.setAttribute("user", (UserVO)user.get());
			return "admin/product/list";
		}
			
			return "redirect:adminlogin";
		
	}

	
	
}
