package com.goodmeaning.controller;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.OrderService;
import com.goodmeaning.vo.UserVO;
@Controller
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	UserRepository urepo;
	
	@RequestMapping("/order")//브라우저에서 요청하는 코드
	public String selectAll(  Long[] cartNum, HttpSession session, Model model) {
		
		System.out.println( Arrays.toString(cartNum) );
		
		// session의 id
		UserVO user = urepo.findById("01011114444").get();
    	session.setAttribute("user", user);
    	//UserVO user = (UserVO)session.getAttribute("user");    
    	
		model.addAttribute("olist", orderService.findOrderList(cartNum));		
		return "user/order/order";		
	}
	
	/*public class HomeController {
		
		private IamportClient api;
		
		public HomeController() {
	    	// REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
			this.api = new IamportClient("","");
		}
			
		@ResponseBody
		@RequestMapping(value="/verifyIamport/{imp_uid}")
		public IamportResponse<Payment> paymentByImpUid(
				Model model
				, Locale locale
				, HttpSession session
				, @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException
		{	
				return api.paymentByImpUid(imp_uid);
		}
		
	}*/
	
}
