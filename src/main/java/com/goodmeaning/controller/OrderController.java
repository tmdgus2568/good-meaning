package com.goodmeaning.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.OrderService;
import com.goodmeaning.service.ProductService;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;

@Controller
public class OrderController {

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	UserRepository urepo;

	@Autowired
	ProductRepository prepo;

	@Autowired
	ProductOptionRepository porepo;

	@RequestMapping("/order") // 브라우저에서 요청하는 코드
	public String selectAll(Long[] cartNum, HttpSession session, Model model) {

		System.out.println(Arrays.toString(cartNum));
		UserVO user = (UserVO) session.getAttribute("user");

		model.addAttribute("olist", orderService.findOrderList(cartNum));
		return "user/order/order";
	}

	@GetMapping("/ordernow")	
	public String goOrderNow(
			@RequestParam(value = "optionnum[]", required = false) List<Long> optionnum,
			@RequestParam(value = "opquantity[]", required = false) List<Integer> opquantity, Long pronum,
			Integer proquantity, HttpSession session, Model model) {
		System.out.println("pronum=" + pronum);
		System.out.println("opquantity=" + opquantity);
		UserVO user = (UserVO)session.getAttribute("user"); 
		ProductVO product = productService.selectById(pronum);
		List<ProductOptionVO> optionList = new ArrayList<>();
		 
		if (opquantity == null) {//옵션이 없을 때 상품에서 가격 가져옴
				model.addAttribute("proquantity", proquantity);
				System.out.println("proquantity=" + proquantity);
		}else { 
			
	 
			for (int i = 0; opquantity != null && i < opquantity.size(); i++) {
				ProductOptionVO option = porepo.findById(optionnum.get(i)).get();
				optionList.add(option);
			}
	
		}
		model.addAttribute("product", product);//productVO
		model.addAttribute("optionList", optionList);//optionVO
		model.addAttribute("opquantity", opquantity);
		return "user/order/ordernow";
	}

}
