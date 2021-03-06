package com.goodmeaning.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.goodmeaning.aws.S3Service;
import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewAnswerRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.util.UpLoadFileUtils;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewAnswerVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserVO;

@Controller
public class ReviewController {

	@Value("${spring.servlet.multipart.location}")
	String locationPath;

	@Autowired
	UserRepository urepo;

	@Autowired
	ProductRepository prepo;
	
	@Autowired
	ProductOptionRepository proptepo;

	@Autowired
	ReviewRepository rrepo;

	@Autowired
	ReviewAnswerRepository rarepo;

	@Autowired
	OrderRepository orepo;

	@Autowired
	OrderDetailRepository odrepo;
	
	@Autowired
	S3Service s3Service;

	@RequestMapping(value = "/productReview", method = RequestMethod.GET)
	public String selectAllReview(@RequestParam Map<String, Object> param, Model model, HttpSession session) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null);
		UserVO user = (UserVO) session.getAttribute("user"); // ????????? ?????? ?????? ??????

		if (user != null) {
			OrderDetailVO ordetail = odrepo.selectByOrder2(product.getProductNum(), user.getUserPhone());
			model.addAttribute("ordetail", ordetail);
		}

		List<ReviewVO> rlist = rrepo.findByProductNum(product);

		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);
		model.addAttribute("user", user);

		return "user/product/productreview";

	}

	// @ResponseBody
	@RequestMapping(value = "writeReviewReply", method = RequestMethod.POST)
	public String writeReviewReply(@RequestParam Map<String, Object> param, Model model, Long productNum,
			HttpSession session) {

		String ranswerContent = (String) param.get("reviewContent");
		Long reviewno = Long.parseLong((String) param.get("reviewno"));

		System.out.println(ranswerContent);
		System.out.println(reviewno);

		ReviewVO review = rrepo.findById(reviewno).get();
		// UserVO user = urepo.findByUserId("abc").get(); // ?????? session?????? get??? ???
		UserVO user = (UserVO) session.getAttribute("user");

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
	public String reviewWriteForm(@RequestParam Map<String, Object> param, Model model, HttpSession session) {

		Long pno = Long.parseLong((String) param.get("pno"));
		ProductVO product = prepo.findById(pno).orElse(null); // ????????? ???????????? ?????? ??????

		UserVO user = (UserVO) session.getAttribute("user"); // ?????? session?????? get ??? ???

		List<Object[]> objlist = odrepo.selectByOrder(product.getProductNum(), user.getUserPhone());

		Object[] obj = objlist.get(0);
		
		System.out.println(objlist.size());
		System.out.println(objlist.get(0)[0]);
		System.out.println(objlist.get(0)[1]);
		System.out.println(objlist.get(0)[2]);
		System.out.println(objlist.get(0)[3]);
		System.out.println(objlist.get(0)[4]);
		System.out.println(objlist.get(0)[5]);
		System.out.println(objlist.get(0)[6]);
		
		ProductOptionVO proopt = null;
		
		if(obj[5] != null) {
			proopt = proptepo.findById(((BigInteger)obj[5]).longValue()).get();
		}
		
		
		OrderDetailVO orderdetail = OrderDetailVO.builder()
					.orderDetailNum(((BigInteger)obj[0]).longValue())
					.orderDetailPrice((Integer)obj[1])
					.orderDetailQuantity((Integer)obj[2])
					//.orderNum((OrderVO)obj[3])
					//.productNum((ProductVO)obj[4])
					.productOption(proopt)
					.build();
		
		model.addAttribute("date",(Date)obj[6]);
		model.addAttribute("product", product);
		model.addAttribute("user", user);
		model.addAttribute("orderdetail", orderdetail);

		return "user/product/productReviewForm";
	}

	@PostMapping("/reviewinsert")
	public String reviewinsert(ReviewVO review, Long productNum2, Model model, Long recentOrderDetail2,
			HttpSession session) {

		ProductVO product = prepo.findById(productNum2).get();
		review.setProductNum(product);

		OrderDetailVO orderdetail = odrepo.findById(recentOrderDetail2).get();
		review.setOrderDetail(orderdetail);

		UserVO user = (UserVO) session.getAttribute("user");
		review.setUserPhone(user);

		System.out.println(review);

		MultipartFile[] uploadfiles = review.getUploadFile(); // max 2??? ???????????? ?????????
		List<String> fileNames = new ArrayList<>();

		// ???????????? ???????????? ????????? ?????? = /uploadPath/imgUpload (??????????????? ???????????? ???)
		String uploadPath = locationPath + File.separator + "reviewupload";
		int i = 0;

		if (uploadfiles != null) {

			for (MultipartFile uploadfile : uploadfiles) {
				// ????????? ????????? ??? vo?????????
				String fileName = null;
				if (uploadfile.getOriginalFilename() != null && !uploadfile.getOriginalFilename().equals("")) {
					try {

						fileName = "reviewupload" + File.separator + UpLoadFileUtils.makeUniqueFileName(uploadfile.getOriginalFilename());
						System.out.println("fileName=" + fileName);
						
						s3Service.uploadFile(uploadfile, fileName);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// ????????? ????????? ?????????
					continue;
				}
				fileNames.add(fileName);

				if (i == 0)
					review.setReviewMainimg1(fileName);
				if (i == 1)
					review.setReviewMainimg2(fileName);

				i++;

			}
		}

		rrepo.save(review);
		// return "redirect:productReview?pno=" + productNum2;

		List<ReviewVO> rlist = rrepo.findByProductNum(product);

		model.addAttribute("rlist", rlist);
		model.addAttribute("product", product);

		return "user/product/productreview";

	}

}