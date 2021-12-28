package com.goodmeaning.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;

@Controller
public class ReviewController {

	@Autowired
	ProductRepository prepo;

	@Autowired
	ReviewRepository rrepo;

	@RequestMapping(value = "productreview", method = RequestMethod.POST)
	public String selectAllReview(@RequestParam Map<String, Object> param, Model model) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null);
		List<ReviewVO> rlist = rrepo.findByProductNum(product);
		model.addAttribute("rlist", rlist);
		
		System.out.println(rlist);

		return "user/product/productreview";
	}

}