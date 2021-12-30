package com.goodmeaning.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.security.UserService;
import com.goodmeaning.service.RegisterService;
import com.goodmeaning.vo.UserVO;

@Controller
public class RegisterController {
	@Autowired
	RegisterService registerService;
	@Autowired
	UserService userService;
	
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
	
	// 회원가입 - 회원가입창 (네이버) 
	@RequestMapping(value = "/register", method = RequestMethod.GET, params = "method=naver")
	public String registerNaverForm(Model model, HttpServletRequest request) {
		 Map<String, ?> flashMap =RequestContextUtils.getInputFlashMap(request);
	        
	     if(flashMap!=null) {
	    	 Map<String, Object> userInfo = (Map<String, Object>) flashMap.get("userInfo");
	     	 model.addAttribute("userId",userInfo.get("userId").toString());
	     	 model.addAttribute("userEmail", userInfo.get("userEmail").toString());
	     	 model.addAttribute("userName", userInfo.get("userName")).toString();
	     	 model.addAttribute("userPhone1", userInfo.get("userPhone1")).toString();
	     	 model.addAttribute("userPhone2", userInfo.get("userPhone2")).toString();
	     	 model.addAttribute("userPhone3", userInfo.get("userPhone3")).toString();
	     	 model.addAttribute("userBirth", userInfo.get("userBirth")).toString();
	            
	      }
		return "user/register/register";
	}
	
	
	// 회원가입 - 회원가입 완료
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(UserVO user) {
		System.out.println("user : " + user);
		userService.joinUser(user);
		return "redirect:/auth/login";
	}
	
	// 중복확인 (ajax)
	@RequestMapping("/register/checkUserId")
	@ResponseBody
	public Map<String, Object> checkUserId(String userId){
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", registerService.checkUserId(userId));
		return map;
		
	}

	
	

}
