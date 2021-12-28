package com.goodmeaning.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Predicate;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.vo.Category;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;

@Controller
public class mjProductController {

	@Autowired
	ProductRepository prepo;

	@Autowired
	ProductOptionRepository porepo;

	@RequestMapping("/productlist")
	public String selectAll(Model model, PageVO pageVO, HttpSession session, HttpServletRequest request,
			Category category) {

		Predicate p = prepo.makePredicate(pageVO.getType(), pageVO.getKeyword()); // pvo변수로 받아온 값으로 주기
		// Pageable pageable = PageRequest.of(0, 3);
		Pageable pageable = pageVO.makePaging(pageVO.getPage(), "productNum"); // bno 기준으로 desc정렬
		Page<ProductVO> result = prepo.findAll(p, pageable);

		model.addAttribute("category", category);
		model.addAttribute("plist", result);
		model.addAttribute("pagevo", pageVO); // 추가
		model.addAttribute("result", new PageMaker<>(result));

		return "user/product/productlist";
	}

	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	public String selectById(Long pno, Model model, ProductVO productVO) {
		ProductVO product = prepo.findById(pno).orElse(null);
		List<ProductOptionVO> options = porepo.findByProductNum(product).orElse(null);
		model.addAttribute("product", product);
		model.addAttribute("productVO", productVO);
		model.addAttribute("options", options);

		return "user/product/productdetail";
	}

	/*
	 * @RequestMapping(value = "/productreview", method = RequestMethod.GET) public
	 * String review(Long pno, Model model) { ProductVO product =
	 * prepo.findById(pno).get();
	 * 
	 * List<ReviewVO> reviewlist = rvrepo.findByProduct(product);
	 * 
	 * model.addAttribute("reviewlist", reviewlist);
	 * 
	 * return "user/product/productdetail"; }
	 */
}
