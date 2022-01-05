package com.goodmeaning.controller;

import java.util.Arrays;
import java.util.List;

import java.util.Map;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.OrderService;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;
@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	ProductRepository prepo;

	@Autowired
	ProductOptionRepository porepo;
	
	
	@RequestMapping("/order")//브라우저에서 요청하는 코드
	public String selectAll(Long[] cartNum, HttpSession session, Model model) {
		
		System.out.println( Arrays.toString(cartNum) );
		
		// session의 id
//		UserVO user = urepo.findById("01011114444").get();
//    	session.setAttribute("user", user);
    	UserVO user = (UserVO)session.getAttribute("user");    
    	
		model.addAttribute("olist", orderService.findOrderList(cartNum));		
		return "user/order/order";		
	}
	
	@PostMapping("/ordernow/")
	public String goOrderNow(Long productNum, HttpSession session, Model model) {
		ProductVO product = prepo.findById(productNum).orElse(null);
		
		List<ProductOptionVO> options = porepo.findByProductNum(product);
		
	
		model.addAttribute("product", product);
		model.addAttribute("options", options);
		//model.addAttribute("productVO", productVO);
		System.out.println("model"+product);
		System.out.println("model"+options);
		UserVO user = (UserVO)session.getAttribute("user"); 
		return "user/order/ordernow";
	}
	
	
	@RequestMapping(value = "/buyclick", method = RequestMethod.POST)
	@ResponseBody
	public void buying (@RequestParam(value="optionnum[]", required = false) List<String> optionnum
						,@RequestParam(value="opquantity[]", required = false) List<String> opquantity
						, String pronum
						, String proquantity) {
		
		System.out.println(pronum);
		System.out.println(proquantity);
		System.out.println(optionnum);
		System.out.println(opquantity);
		
		
		
	}
}
