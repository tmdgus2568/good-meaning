package com.goodmeaning.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.vo.ProductVO;


@Controller
public class mjProductController {

	@Autowired
	ProductRepository prepo;
	
	@RequestMapping("/productlist")
	public String selectAll(ProductVO productVO, Model model) {
		model.addAttribute("plist", prepo.findAll());
		return "user/product/productlist";
	}
	
	
}
