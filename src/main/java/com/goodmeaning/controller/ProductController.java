package com.goodmeaning.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Predicate;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.util.UpLoadFileUtils;
import com.goodmeaning.vo.Category;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;

@Controller
public class ProductController {

	@Autowired
	ProductRepository prepo;

	@Autowired
	ProductOptionRepository porepo;

	@RequestMapping("/productlist/{cate}")
	public String selectAll(@PathVariable("cate") String cate, Model model, PageVO pageVO, HttpSession session, HttpServletRequest request,
			Category category) {

		System.out.println(cate);
		if(!(cate.equals("ALL"))){
		System.out.println(Category.valueOf(cate));
		}
		
		Predicate p = prepo.makePredicate(pageVO.getType(), pageVO.getKeyword()); // pvo변수로 받아온 값으로 주기
		// Pageable pageable = PageRequest.of(0, 3);
		Pageable pageable = pageVO.makePaging(pageVO.getPage(), "productNum"); // bno 기준으로 desc정렬
		Page<ProductVO> result = prepo.findAll(p, pageable);

		model.addAttribute("plist", result);
		model.addAttribute("pagevo", pageVO); // 추가
		model.addAttribute("result", new PageMaker<>(result));

		return "user/product/productlist";
	}

	@RequestMapping(value = "/productdetail", method = RequestMethod.GET)
	public String selectById(Long pno, Model model, ProductVO productVO) {
		ProductVO product = prepo.findById(pno).orElse(null);

		List<ProductOptionVO> options = porepo.findByProductNum(product);

		model.addAttribute("product", product);
		model.addAttribute("productVO", productVO);
		model.addAttribute("options", options);

		return "user/product/productdetail";
// 리뷰삭제 //리뷰수정
	}
}
