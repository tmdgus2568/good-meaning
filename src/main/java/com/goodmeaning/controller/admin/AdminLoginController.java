package com.goodmeaning.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.service.LoginService;
import com.goodmeaning.service.ProductService;
import com.goodmeaning.service.admin.AdminLoginService;
import com.goodmeaning.service.admin.AdminProductService;
import com.goodmeaning.util.UpLoadFileUtils;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;

@Controller
@RequestMapping("/admin/")
public class AdminLoginController {
	
	//브라우저 해석 : static //서버해석 : templates
	
	
	@Autowired
	AdminLoginService loginService;
	
	//로그인 페이지 
	@GetMapping ("/") //주소창에서 들어오는것! 
	public String getLoginPage() {
		return "admin/auth/login";
	}
	
	//요청방식이 다르기때문에 맵핑주소 같아도 됨
	//로그인 실행 ****************************관리자 로그인 service 만들기!!!!!!!!!
	@PostMapping("/")
	public String login(String userId, String userPw, HttpSession session) {
		// 로그인 성공시 
		Optional<UserVO> user = loginService.checkLogin(userId, userPw);
		System.out.println(user);
		if(user.isPresent()) {
			session.setAttribute("user", (UserVO)user.get());
			return "redirect:/";
		}
		return "redirect:list";
	}

	
	
}
