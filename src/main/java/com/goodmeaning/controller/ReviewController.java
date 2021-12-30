package com.goodmeaning.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewAnswerRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewAnswerVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserVO;

@Controller
public class ReviewController {

	@Autowired
	UserRepository urepo;
	
	@Autowired
	ProductRepository prepo;

	@Autowired
	ReviewRepository rrepo;
	
	@Autowired
	ReviewAnswerRepository rarepo;
	
	@Autowired
	OrderRepository orepo;
	
	@Autowired
	OrderDetailRepository odrepo;

	@RequestMapping(value = "productReview", method = RequestMethod.POST)
	public String selectAllReview(@RequestParam Map<String, Object> param, Model model) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null);

		List<ReviewVO> rlist = rrepo.findByProductNum(product);
		System.out.println(rlist.get(0).getAnswers().size());
		System.out.println(rlist.get(1).getAnswers().size());
		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);
		System.out.println(rlist);

		return "user/product/productreview";
	}

	//@ResponseBody
	@RequestMapping(value = "writeReviewReply", method = RequestMethod.POST)
	public  String writeReviewReply(@RequestParam Map<String, Object> param,
			Model model, Long productNum) {

		String ranswerContent = (String) param.get("reviewContent");
		Long reviewno = Long.parseLong((String) param.get("reviewno"));
		
		System.out.println(ranswerContent);
		System.out.println(reviewno);
		
		ReviewVO review = rrepo.findById(reviewno).get();
		UserVO user = urepo.findByUserId("abc").get();		//추후 session에서 get할 것

		System.out.println(review);
		System.out.println(user);
		
		ReviewAnswerVO reply = ReviewAnswerVO.builder()
				.ranswerContent(ranswerContent)
				.reviewNum(review)
				.userPhone(user)
				.build();

		reply = rarepo.save(reply);
		
		
		ProductVO product = prepo.findById(productNum).get();
		
		//return new ResponseEntity<>(reply, HttpStatus.CREATED);
		List<ReviewVO> rlist = rrepo.findByProductNum(product);
		
		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);
		return "user/product/productreview";
		 
	}
	
	@RequestMapping(value = "reviewWriteForm", method = RequestMethod.POST)
	public String reviewWriteForm (@RequestParam Map<String, Object> param, Model model) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null);

		UserVO user = urepo.findByUserId("abc").get();		//추후 session에서 get할 것
		List<OrderVO> orderlist = orepo.findByUserPhone(user);	//작성 됐는지 안 됐는지 어떻게 구문하지
		
		List<List<OrderDetailVO>> orderdetaillist = null;	//중첩 List
		
		for(OrderVO o : orderlist) {
			orderdetaillist.add(odrepo.findByOrderNum(o));
		}
		
		model.addAttribute("product", product);
		model.addAttribute("orderlist", orderlist);
		model.addAttribute("orderdetaillist", orderdetaillist);

		return "user/product/productReviewForm";
	}
	
}