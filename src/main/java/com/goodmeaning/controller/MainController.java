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
		List<ProductVO> bestlist = service.findBestlist();
		List<ProductVO> newlist = service.findNewlist();
		
		List<ProductVO> newlist1;
		List<ProductVO> newlist2;
		
		List<ProductVO> bestlist1;
		List<ProductVO> bestlist2;
		
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
		
		if(bestlist.size()>=5) {
			bestlist1 = bestlist.subList(0, 4);
			bestlist2 = bestlist.subList(4, bestlist.size());
			
			model.addAttribute("bestlist1", bestlist1);
			model.addAttribute("bestlist2", bestlist2);
		}
		else {
			bestlist1 = bestlist.subList(0, bestlist.size());
			
			model.addAttribute("bestlist1", bestlist1);
		
			
		}
		
		
		return "/user/main/main";
	}
}
