package com.goodmeaning.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.security.UserService;
import com.goodmeaning.service.RegisterService;
import com.goodmeaning.vo.UserVO;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Controller
public class RegisterController {
	@Autowired
	RegisterService registerService;
	@Autowired
	UserService userService;
	
	// 트윌리오 
	public static final String ACCOUNT_SID = "AC771abf36be029f37c310af1af666fe92";
	public static final String AUTH_TOKEN = "915e1eac4369c3403d0feeb954688d57";
	

	// 회원가입 - 회원가입창 (이용동의를 넘어서 user를 가져와야하기 때문에 post로)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerForm(@RequestParam Map<String, Object> userInfo, Model model) {
		
		for(String key:userInfo.keySet()) {
			if(userInfo.get(key)!="" && userInfo.get(key)!=null) {
				model.addAttribute(key,userInfo.get(key));
			}
		}
		return "user/register/register";
	}
	// 회원가입 - 동의사항 (일반회원)
	@RequestMapping(value = "/register/agree")
	public String registerAgreeNormal() {
		return "user/register/registerAgree";
	}

	
	// 회원가입 - 동의사항 (카카오) 
	@RequestMapping(value = "/register/agree", method = RequestMethod.GET, params = "method=kakao")
	public String registerAgreeKakao(Model model, HttpServletRequest request) {
		 Map<String, ?> flashMap =RequestContextUtils.getInputFlashMap(request);
	        
	     if(flashMap!=null) {
	    	 Map<String, Object> userInfo = (Map<String, Object>) flashMap.get("userInfo");
	     	 model.addAttribute("userId",userInfo.get("id").toString());
	     	 System.out.println("id : " + userInfo.get("id").toString());
	     	 
	     	 // 이메일은 동의를 안했으면 직접 입력하게 해야하므로 
	     	 if(userInfo.get("email")!=null) {
	     		model.addAttribute("userEmail", userInfo.get("email").toString());
	     		System.out.println("email : " + userInfo.get("email").toString());
	     	 }
	            
	      }
		return "user/register/registerAgree";
	}
	
	// 회원가입 - 동의사항 (네이버) 
	@RequestMapping(value = "/register/agree", method = RequestMethod.GET, params = "method=naver")
	public String registerAgreeNaver(Model model, HttpServletRequest request) {
		 Map<String, ?> flashMap =RequestContextUtils.getInputFlashMap(request);
	        
	     if(flashMap!=null) {
	    	 Map<String, Object> userInfo = (Map<String, Object>) flashMap.get("userInfo");
	     	 model.addAttribute("userId",userInfo.get("userId").toString());
	     	 model.addAttribute("userEmail", userInfo.get("userEmail").toString());
	     	 model.addAttribute("userName", userInfo.get("userName")).toString();
	     	 model.addAttribute("userPhone1", userInfo.get("userPhone1")).toString();
	     	 model.addAttribute("userPhone2", userInfo.get("userPhone2")).toString();
	     	 model.addAttribute("userPhone3", userInfo.get("userPhone3")).toString();
	     	 model.addAttribute("userBirth", userInfo.get("userBirth")).toString();
	            
	      }
		return "user/register/registerAgree";
	}
	
	
	// 회원가입 - 회원가입 완료
	@RequestMapping(value = "/register/insert", method = RequestMethod.POST)
	public String register(UserVO user) {
		System.out.println("user : " + user);
		userService.joinUser(user);
		return "redirect:/auth/login";
	}
	
	// 중복확인 (ajax)
	@RequestMapping("/register/checkUserId")
	@ResponseBody
	public Map<String, Object> checkUserId(String userId){
		Map<String, Object> map = new HashMap<>();
		
		map.put("result", registerService.checkUserId(userId));
		return map;
		
	}
	
	// twillo 문자인증 
	@RequestMapping(value="/register/authUserPhone", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> authUserPhone(String userPhone){
		Map<String, Object> map = new HashMap<>();
		
		map.put("authNum",sendSMS("82",userPhone));
		return map;
		
	}
	 
	  // SMS 전송
	public static int sendSMS (String country, String phoneNum) {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    
	    // 휴대폰 인증번호 생성
	    int authNum = randomRange(100000, 999999);
	    
	    
	    // 전송대상 휴대폰 번호
	    String sendTarget = "+"+ country + phoneNum;
	    
	    // 전송 메세지
	    String authMsg = "굿미닝 휴대폰 인증번호: [" + authNum + "]" ;
	    
	    
	    Message message = Message.creator(
	    	// to
	    	new PhoneNumber(sendTarget),
	        // from
	    	new PhoneNumber("+13027543133"), 
	        // message
	    	authMsg).create();
	    
			return authNum;
		
	  }
	    
	  // 인증번호 범위 지정
	  public static int randomRange(int n1, int n2) {
	    return (int) (Math.random() * (n2 - n1 + 1)) + n1;
	  }

	
	

}
