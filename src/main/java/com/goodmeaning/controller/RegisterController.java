package com.goodmeaning.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

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
	
	// 회원가입 - 회원가입창 (카카오) 
	@RequestMapping(value = "/register", method = RequestMethod.GET, params = "method=kakao")
	public String registerKakaoForm(Model model, HttpServletRequest request) {
		 Map<String, ?> flashMap =RequestContextUtils.getInputFlashMap(request);
	        
	     if(flashMap!=null) {
	    	 Map<String, Object> userInfo = (Map<String, Object>) flashMap.get("userInfo");
	     	 model.addAttribute("userId",userInfo.get("id").toString());
	     	 System.out.println("id : " + userInfo.get("id").toString());
	     	 
	     	 // 이메일은 동의를 안했으면 직접 입력하게 해야하므로 
	     	 if(userInfo.get("email")!=null) {
	     		model.addAttribute("userEmail", userInfo.get("email").toString());
	     		System.out.println("email : " + userInfo.get("email").toString());
	     	 }
	     	 
	     	 
	     	 
	            
	      }
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
