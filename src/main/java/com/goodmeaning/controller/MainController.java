package com.goodmeaning.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.goodmeaning.service.MainService;
import com.goodmeaning.vo.ProductVO;


@Controller
public class MainController {
	@Autowired
	MainService service;
	
	@GetMapping("/")
	public String main(Model model) {
		List<ProductVO> bestlist = new ArrayList<>();
		List<ProductVO> newlist = service.findNewlist();
		
		List<ProductVO> newlist1;
		List<ProductVO> newlist2;
		
		if(newlist.size()>=5) {
			newlist1 = newlist.subList(0, 4);
			newlist2 = newlist.subList(4, newlist.size());
			
			model.addAttribute("newlist1", newlist1);
			model.addAttribute("newlist2", newlist2);
		}
		else {
			newlist1 = newlist.subList(0, newlist.size());
			
			model.addAttribute("newlist1", newlist1);
		
			
		}
		System.out.println(newlist);
		
		
		return "/user/main/main";
	}
}
