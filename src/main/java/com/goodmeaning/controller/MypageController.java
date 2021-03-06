package com.goodmeaning.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.aws.S3Service;
import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.MypageService;
import com.goodmeaning.util.UpLoadFileUtils;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserVO;
import com.querydsl.core.types.Predicate;

@Controller
public class MypageController {
	
	
	@Autowired
	MypageService mypageService;
	@Autowired
	OrderRepository orderRepo;
	@Autowired
	ReviewRepository reviewRepo;
	@Autowired
	UserRepository urepo;
	
	@Autowired
	ProductRepository prepo;
	
	@Autowired
	ProductOptionRepository optionRepo;
	
	@Autowired
	OrderDetailRepository detailRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder; // security config?????? Bean??????
	
	@Autowired
	S3Service s3Service;

	
	// ???????????? 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.GET)
	public String updateUserForm(Model model, HttpSession session) {

		UserVO user = (UserVO) session.getAttribute("user");
		
		Optional<UserVO> userInfo = mypageService.findUser(user.getUserPhone());
		
		if(userInfo.isPresent()) {
			model.addAttribute("user",userInfo.get());
			System.out.println("userInfo: " + userInfo.get());
		}
	
		return "user/mypage/update";
	}

	// ???????????? 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.POST)
	public String updateUser(UserVO user, HttpSession session) {
		System.out.println(user);
		// session??? ?????? user ???????????? 
		UserVO sessionUser = (UserVO) session.getAttribute("user");
		// session??? ????????? ??????????????? ???????????? ?????????
		if(!sessionUser.getUserPw().equals(user.getUserPw()))
			user.setUserPw(passwordEncoder.encode(user.getUserPw()));
		
		mypageService.updateUser(user);
		return "redirect:/mypage/user";
	}

	//???????????? ??? orderVO, orderDetailVO ??????
	@PostMapping("/orderSave")
	@ResponseBody
	public String orderSaveMethod(OrderVO orderVO,  HttpSession session,
		 @RequestParam(value = "orderDetailQuantity[]") String[] orderDetailQuantity,
		 @RequestParam(value = "orderDetailPrice[]")  String[] orderDetailPrice,
		 @RequestParam(value = "productNum[]") String[] productNum, 
		 @RequestParam(value = "productOption[]") String[] productOption   ) {
	    System.out.println(orderVO);
	    System.out.println(Arrays.toString(orderDetailQuantity));
	    System.out.println(Arrays.toString(orderDetailPrice));
	    System.out.println(Arrays.toString(productNum));
	    System.out.println(Arrays.toString(productOption));
		//OrderVo?????? 
	    UserVO sessionUser = (UserVO) session.getAttribute("user");
	    orderVO.setUserPhone(sessionUser);
	    OrderVO newOrder =   orderRepo.save(orderVO);
	    System.out.println("newOrder=" + newOrder);
	    for(int i=0; i<productNum.length; i++) {
	    	ProductVO product = prepo.findById(Long.valueOf(productNum[i])).get();
	    	ProductOptionVO optionVO = optionRepo.findById(Long.valueOf(productOption[i])).orElse(null);
	    	OrderDetailVO detailVO = OrderDetailVO.builder()
	    			.orderDetailQuantity(Integer.valueOf(orderDetailQuantity[i]))
	    			.orderDetailPrice(Integer.valueOf(orderDetailPrice[i]))
	    			.productNum(product)
	    			.productOption(optionVO)
	    			.orderNum(newOrder)
	    			.build();
	    	System.out.println("detailVO=" + detailVO);
	    	detailRepo.save(detailVO);
	    }
	    	    
	    return "????????????";
	}
	// ?????? ?????? ??????
	// ???????????? / ??????????????? / ???????????? 
	@RequestMapping(value = "/mypage/orders", method = RequestMethod.GET)
	public String orders(Model model, HttpSession session, PageVO pageVO, HttpServletRequest request) {
		
		
		UserVO user = (UserVO)session.getAttribute("user");

		String[] types = {"userPhone","orderStatus"};
				
		Object[] keywords = {user,"orders"};

		pageVO = makePage(types, keywords, pageVO, model, request);

		
		Pageable paging = pageVO.makePaging(0, "orderNum"); // sort Direction, sort??? ??????
		Predicate pre = orderRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<OrderVO> result = orderRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result).getResult().getContent());
		model.addAttribute("orders", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/orders";
	}
	
	// ?????? ?????? ?????? ?????? 
	@RequestMapping(value = "/mypage/orders/{oid}", method = RequestMethod.GET)
	public String orderDetails(@PathVariable long oid, Model model) {
		Optional<OrderVO> order = mypageService.findOrderById(oid);
		int sum = 0;
		
		if(order.isPresent()) {
			model.addAttribute("order",(OrderVO)order.get());
		
		}
		return "/user/mypage/orderdetail";
	}
	
	// ???????????? ?????????
	@RequestMapping(value = "/mypage/orders/{oid}", method = RequestMethod.POST)
	public String orderCancel(@PathVariable long oid, RedirectAttributes rattrs) {
		Optional<OrderVO> order = mypageService.findOrderById(oid);
		if(order.isPresent()) {
			order.get().setOrderStatus("????????????");
			mypageService.updateOrder(order.get());
			rattrs.addFlashAttribute("result","success");
		}
		else rattrs.addFlashAttribute("result","failed");
		return "redirect:/mypage/orders";
	}
	
	
	// ?????? ?????? ?????????
	@RequestMapping(value="/mypage/review/create", method = RequestMethod.GET)
	public String createReviewForm(long orderDetailNum, Model model, HttpSession session) {
		System.out.println(orderDetailNum);
		Optional<OrderDetailVO> detail = mypageService.findOrderDetailById(orderDetailNum);
		if(detail.isPresent()) {
			model.addAttribute("user", (UserVO)session.getAttribute("user"));
			model.addAttribute("recentOrderDetail", detail.get());
			model.addAttribute("product", detail.get().getProductNum());
			model.addAttribute("recentOrder", detail.get().getOrderNum());
		}
		
		return "/user/mypage/createreview";
	}
	
	@RequestMapping(value="/mypage/review/create", method = RequestMethod.POST)
	public String createReview(ReviewVO review, long productNum, long orderDetail, int isUpdate, HttpSession session, RedirectAttributes rattrs) {

		System.out.println("review1 : "+review);
	

		UserVO user = (UserVO) session.getAttribute("user");
		review.setUserPhone(user);

		System.out.println(review);

		MultipartFile[] uploadfiles = review.getUploadFile(); // max 2??? ???????????? ?????????
		List<String> fileNames = new ArrayList<>();

		// ???????????? ???????????? ????????? ?????? = /uploadPath/imgUpload (??????????????? ???????????? ???)
//		String uploadPath = locationPath + File.separator + "reviewupload";
		// ??? ?????? ?????? ?????? ?????? ??????
		// String ymdPath = UpLoadFileUtils.calcPath(uploadPath);
		int i = 0;

		if (uploadfiles != null) {

			for (MultipartFile uploadfile : uploadfiles) {
				// ????????? ????????? ??? vo?????????
				String fileName = null;
				if (uploadfile.getOriginalFilename() != null && !uploadfile.getOriginalFilename().equals("")) {
					try {

						fileName = "reviewupload" + File.separator + UpLoadFileUtils.makeUniqueFileName(uploadfile.getOriginalFilename());
						System.out.println("fileName=" + fileName);
						
						s3Service.uploadFile(uploadfile, fileName) ;
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
		
		mypageService.saveReview(review);
		if(isUpdate == 1) {
			rattrs.addFlashAttribute("review_update_result","success");
			return "redirect:/mypage/reviews";
		}
			
		rattrs.addFlashAttribute("review_result","success");
		return "redirect:/mypage/orders";
			
		
		
	}
	
	
	
	// ???????????? ?????? ?????? 
	// ???????????? ????????? ???????????? ??????????????? ?????? ????????? ??????. 
	// ???????????? / ?????? / ?????? 
	@RequestMapping(value = "/mypage/updateorders", method = RequestMethod.GET)
	public String updateorders(Model model, HttpSession session, PageVO pageVO, HttpServletRequest request, String filter) {
		UserVO user = (UserVO)session.getAttribute("user");

		String[] types = {"userPhone","orderStatus"};
		
		Object[] keywords = {user,"updateorders"}; // default
		
		System.out.println(filter);
		
		if(filter != null) {

			keywords[1] = filter;
		}

	    pageVO = makePage(types, keywords, pageVO, model, request);

		
		Pageable paging = pageVO.makePaging(0, "orderNum"); // sort Direction, sort??? ??????
		Predicate pre = orderRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<OrderVO> result = orderRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result));
		model.addAttribute("orders", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/updateorders";
	}
	
	
	// ?????? ?????? ???????????? ???????????? 
	// 
	@RequestMapping(value = "/mypage/reviews", method = RequestMethod.GET)
	public String reviews(Model model, HttpSession session, PageVO pageVO, HttpServletRequest request) {
		
		
		UserVO user = (UserVO)session.getAttribute("user");

		String[] types = {"userPhone"};
		Object[] keywords = {user};

		pageVO = makePage(types, keywords, pageVO, model, request);
		
		Pageable paging = pageVO.makePaging(0, "reviewNum"); // sort Direction, sort??? ??????
		Predicate pre = reviewRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<ReviewVO> result = reviewRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result));
		model.addAttribute("reviews", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/reviews";
	}
	
	// ???????????? ????????? ??? modal ????????? 
	@RequestMapping("/mypage/review")
	@ResponseBody
	public ReviewVO review(long no) {
		Optional<ReviewVO> review = mypageService.review(no);
		if(review.isPresent()) return review.get();
		return null;
		
	}
	
	// ?????? ?????? ?????????
	@RequestMapping(value="/mypage/review/update", method = RequestMethod.GET)
	public String updateReviewForm(long reviewNum, Model model, HttpSession session) {
		
		Optional<ReviewVO> review = mypageService.findReviewtById(reviewNum);
		if(review.isPresent()) {
			model.addAttribute("user", (UserVO)session.getAttribute("user"));
			model.addAttribute("recentOrderDetail", review.get().getOrderDetail());
			model.addAttribute("product", review.get().getProductNum());
			model.addAttribute("recentOrder", review.get().getOrderDetail().getOrderNum());
			model.addAttribute("review", review.get());
		}
		
		return "/user/mypage/createreview";
	}
	
	
	public PageVO makePage(String[] types, Object[] keywords, PageVO pageVO, Model model, HttpServletRequest request) {
		// RedirectAttributes??? ????????? ??????
		PageVO pageVO2 = null;
		 
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
		if (flashMap != null) {
			String resultmsg = (String) flashMap.get("resultmsg");
			model.addAttribute("msg", resultmsg);
	
			pageVO2 = (PageVO) flashMap.get("pageVO");
			if(pageVO2!=null) {
				pageVO = pageVO2;
			} 
		}

		
		System.out.println(pageVO);
		
		
		// ???????????? ?????? ??? 
		if(pageVO.getType() == null) {
			pageVO = PageVO.builder().page(pageVO.getPage()).size(5).type(types).keyword(keywords).build();
	
		}
		
		// filter ???????????? ??? -> user??? ???????????? ???????????? 
		else{
			keywords[1] = pageVO.getKeyword()[0];
			
			pageVO = PageVO.builder().page(pageVO.getPage()).size(5).type(types).keyword(keywords).build();
	
		}
		
		return pageVO;
	}
	
	

}
