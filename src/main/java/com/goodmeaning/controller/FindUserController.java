package com.goodmeaning.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.goodmeaning.service.FindUserService;

@Controller
public class FindUserController {
	@Autowired
	FindUserService service;
	
	@RequestMapping("/auth/find")
	public String findForm() {
		return "/user/auth/find";
	}
	
	@RequestMapping(value="/auth/find/id", method = RequestMethod.POST)
	public String findUserId(String userPhone, String userName, String userEmail, Model model) {
	
		String id = service.findId(userPhone, userName, userEmail);
		
		if(id.equals("")) 
			model.addAttribute("result","failed");
		else {
			model.addAttribute("result","success");
			model.addAttribute("userId",id);
		}
			
		
		return "/user/auth/findIdConfirm";
	}

	@RequestMapping(value="/auth/find/pwd", method = RequestMethod.POST)
	public String findUserPs(String userPhone, String userName, String userEmail, String userId, Model model) {
	
		String email;
		try {
			email = service.findPw(userPhone, userName, userEmail, userId);
			if(email.equals("")) 
				model.addAttribute("result","failed");
			else {
				model.addAttribute("result","success");
				model.addAttribute("userEmail",email);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("result","failed");
		
		}
		
	
			
		
		return "/user/auth/findPwConfirm";
	}
}
