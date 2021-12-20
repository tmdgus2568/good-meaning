package com.goodmeaning.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodmeaning.service.MypageService;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.UserVO;

@Controller
public class MypageController {
	@Autowired
	MypageService mypageService;
	
	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.GET)
	public String updateUserForm() {
		
		return "user/mypage/user_update";
	}

	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.POST)
	public String updateUser(UserVO user) {
		System.out.println(user);
		mypageService.update(user);
		return "user/mypage/user_update";
	}

	
	// 주문 내역 확인
	@RequestMapping(value = "/mypage/orders", method = RequestMethod.GET)
	public String orders(Model model, HttpSession session) {
		UserVO user = (UserVO)session.getAttribute("user");
		List<OrderVO> orders = mypageService.orders(user);

		model.addAttribute("orders", orders);
	
		
		return "user/mypage/user_orders";
	}
	

}
