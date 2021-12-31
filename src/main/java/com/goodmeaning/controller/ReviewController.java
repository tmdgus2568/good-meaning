package com.goodmeaning.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	public String selectAllReview(@RequestParam Map<String, Object> param, Model model, HttpSession session) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null);

		UserVO user = (UserVO)session.getAttribute("user"); //로그인 되어있는 유저

		List<ReviewVO> rlist = rrepo.findByProductNum(product);

		List<OrderVO> orderlist = orepo.findByUserPhone(user);
		List<List<OrderDetailVO>> orderdetaillist = new ArrayList<>(); // 중첩 List

		for (OrderVO o : orderlist) {
			orderdetaillist.add(odrepo.findByOrderNum(o));
		}

		Map<String, Object> map = new HashMap<>();

		List<OrderVO> reviewneededlist = new ArrayList<>();
		List<OrderDetailVO> reviewneededdetaillist = new ArrayList<>();

		for (List<OrderDetailVO> od : orderdetaillist) {
			for (int i = 0; i < od.size(); i++) {
				if (od.get(i).getProductNum().getProductNum() == pno) {
					OrderVO order = orepo.findById(od.get(i).getOrderNum().getOrderNum()).get();
					reviewneededlist.add(order);

					OrderDetailVO orderdetail = odrepo.findById(od.get(i).getOrderDetailNum()).get();
					reviewneededdetaillist.add(orderdetail);
				}
			}
		}

		map.put("order", reviewneededlist);
		map.put("orderdetail", reviewneededdetaillist);

		model.addAttribute("product", product);
		model.addAttribute("review", map);

		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);
		model.addAttribute("user", user);

		return "user/product/productreview";
	}

	// @ResponseBody
	@RequestMapping(value = "writeReviewReply", method = RequestMethod.POST)
	public String writeReviewReply(@RequestParam Map<String, Object> param, Model model, Long productNum, HttpSession session) {

		String ranswerContent = (String) param.get("reviewContent");
		Long reviewno = Long.parseLong((String) param.get("reviewno"));

		System.out.println(ranswerContent);
		System.out.println(reviewno);

		ReviewVO review = rrepo.findById(reviewno).get();
		//UserVO user = urepo.findByUserId("abc").get(); // 추후 session에서 get할 것
		UserVO user = (UserVO)session.getAttribute("user");
		
		System.out.println(review);
		System.out.println(user);

		ReviewAnswerVO reply = ReviewAnswerVO.builder().ranswerContent(ranswerContent).reviewNum(review).userPhone(user)
				.build();

		reply = rarepo.save(reply);

		ProductVO product = prepo.findById(productNum).get();

		// return new ResponseEntity<>(reply, HttpStatus.CREATED);
		List<ReviewVO> rlist = rrepo.findByProductNum(product);

		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);
		return "user/product/productreview";

	}

	@RequestMapping(value = "reviewWriteForm", method = RequestMethod.POST)
	public String reviewWriteForm(@RequestParam Map<String, Object> param, Model model) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null); // 리뷰를 작성해야 하는 상품

		UserVO user = urepo.findByUserId("1111").get(); // 추후 session에서 get 할 것

		List<OrderVO> orderlist = orepo.findByUserPhone(user);
		List<List<OrderDetailVO>> orderdetaillist = new ArrayList<>(); // 중첩 List

		for (OrderVO o : orderlist) {
			orderdetaillist.add(odrepo.findByOrderNum(o));
		}

		Map<String, Object> map = new HashMap<>();

		List<OrderVO> reviewneededlist = new ArrayList<>();
		List<OrderDetailVO> reviewneededdetaillist = new ArrayList<>();

		for (List<OrderDetailVO> od : orderdetaillist) {
			for (int i = 0; i < od.size(); i++) {
				if (od.get(i).getProductNum().getProductNum() == pno) {
					OrderVO order = orepo.findById(od.get(i).getOrderNum().getOrderNum()).get();
					reviewneededlist.add(order);

					OrderDetailVO orderdetail = odrepo.findById(od.get(i).getOrderDetailNum()).get();
					reviewneededdetaillist.add(orderdetail);
				}
			}
		}

		map.put("order", reviewneededlist);
		map.put("orderdetail", reviewneededdetaillist);

		model.addAttribute("product", product);
		model.addAttribute("review", map);
		model.addAttribute("user", user);

		return "user/product/productReviewForm";
	}

}