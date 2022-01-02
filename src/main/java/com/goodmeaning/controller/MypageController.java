package com.goodmeaning.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.MypageService;
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
	PasswordEncoder passwordEncoder; // security config에서 Bean생성

	
	// 회원수정 
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

	// 회원수정 
	@RequestMapping(value = "/mypage/user", method = RequestMethod.POST)
	public String updateUser(UserVO user, HttpSession session) {
		System.out.println(user);
		// session에 있는 user 갖고오고 
		UserVO sessionUser = (UserVO) session.getAttribute("user");
		// session과 변경된 비밀번호가 다르다면 인코딩
		if(!sessionUser.getUserPw().equals(user.getUserPw()))
			user.setUserPw(passwordEncoder.encode(user.getUserPw()));
		
		mypageService.updateUser(user);
		return "redirect:/mypage/user";
	}

	//결제완료 후 orderVO, orderDetailVO 생성
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
		//OrderVo완성 
	    //OrderDetailVO완성 (배열갯수만큼)
	    //UserVO user = urepo.findById("01011114444").get();
    	//session.setAttribute("user", user);
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
	    
	    
	    
	    return "주문성공";
	}
	// 주문 내역 확인
	// 입금완료 / 배송준비중 / 배송완료 
	@RequestMapping(value = "/mypage/orders", method = RequestMethod.GET)
	public String orders(Model model, HttpSession session, PageVO pageVO, HttpServletRequest request) {
		
		
		UserVO user = (UserVO)session.getAttribute("user");

		String[] types = {"userPhone","orderStatus"};
				
		Object[] keywords = {user,"orders"};

		pageVO = makePage(types, keywords, pageVO, model, request);

		
		Pageable paging = pageVO.makePaging(0, "orderNum"); // sort Direction, sort할 칼럼
		Predicate pre = orderRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<OrderVO> result = orderRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result).getResult().getContent());
		model.addAttribute("orders", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/orders";
	}
	
	// 주문 상세 정보 확인 
	@RequestMapping(value = "/mypage/orders/{oid}", method = RequestMethod.GET)
	public String orderDetails(@PathVariable long oid, Model model) {
		Optional<OrderVO> order = mypageService.findOrderById(oid);
		int sum = 0;
		
		if(order.isPresent()) {
			model.addAttribute("order",(OrderVO)order.get());
		
		}
		return "/user/mypage/orderdetail";
	}
	
	// 구매취소 누르기
	@RequestMapping(value = "/mypage/orders/{oid}", method = RequestMethod.POST)
	public String orderCancel(@PathVariable long oid, RedirectAttributes rattrs) {
		Optional<OrderVO> order = mypageService.findOrderById(oid);
		if(order.isPresent()) {
			order.get().setOrderStatus("구매취소");
			mypageService.updateOrder(order.get());
			rattrs.addFlashAttribute("result","success");
		}
		else rattrs.addFlashAttribute("result","failed");
		return "redirect:/mypage/orders";
	}
	

	
	
	// 구매취소 항목 확인 
	// 파라미터 이름이 똑같다면 어노테이션 달지 않아도 된다. 
	// 구매취소 / 반품 / 교환 
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

		
		Pageable paging = pageVO.makePaging(0, "orderNum"); // sort Direction, sort할 칼럼
		Predicate pre = orderRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<OrderVO> result = orderRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result));
		model.addAttribute("orders", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/updateorders";
	}
	
	
	// 리뷰 내역 있는것만 불러오기 
	// 
	@RequestMapping(value = "/mypage/reviews", method = RequestMethod.GET)
	public String reviews(Model model, HttpSession session, PageVO pageVO, HttpServletRequest request) {
		
		
		UserVO user = (UserVO)session.getAttribute("user");

		String[] types = {"userPhone"};
		Object[] keywords = {user};

		pageVO = makePage(types, keywords, pageVO, model, request);
		
		Pageable paging = pageVO.makePaging(0, "reviewNum"); // sort Direction, sort할 칼럼
		Predicate pre = reviewRepo.makePredicate(pageVO.getType(), pageVO.getKeyword());
		Page<ReviewVO> result = reviewRepo.findAll(pre, paging);
		System.out.println(new PageMaker<>(result));
		model.addAttribute("reviews", new PageMaker<>(result));
		model.addAttribute("pageVO",pageVO);
	
		
		return "user/mypage/reviews";
	}
	
	// 리뷰확인 눌렀을 때 modal 띄우기 
	@RequestMapping("/mypage/review")
	@ResponseBody
	public ReviewVO review(long no) {
		Optional<ReviewVO> review = mypageService.review(no);
		if(review.isPresent()) return review.get();
		return null;
		
	}
	
	
	public PageVO makePage(String[] types, Object[] keywords, PageVO pageVO, Model model, HttpServletRequest request) {
		// RedirectAttributes를 통해서 받기
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
		
		
		// 아무것도 없을 때 
		if(pageVO.getType() == null) {
			pageVO = PageVO.builder().page(pageVO.getPage()).size(5).type(types).keyword(keywords).build();
	
		}
		
		// filter 들어왔을 때 -> user는 넘어오지 않으므로 
		else{
			keywords[1] = pageVO.getKeyword()[0];
			
			pageVO = PageVO.builder().page(pageVO.getPage()).size(5).type(types).keyword(keywords).build();
	
		}
		
		return pageVO;
	}
	
	

}
