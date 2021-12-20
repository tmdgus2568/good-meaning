package com.goodmeaning.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goodmeaning.service.LoginService;
import com.goodmeaning.vo.UserVO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * 카카오 로그인 로직 : 
 * 	1. 카카오 로그인 api로 연결(a 태그로)
 *  2. 로그인에 성공하면 일회성 인증 코드를 부여해준다 
 *  3. 그 일회성 코드를 "https://kauth.kakao.com/oauth/token"을 통해 넘겨주면 
 *  	값들을 json형태로 받을 수 있다(파싱하여 정보를 가져온다)
 * */
@Controller
public class LoginController {
	private static String domain_url = "http://localhost:8888";
	
	// naver
	private String naverClientId = "BXs_UNlvqj1zkgyIOMnn";
	private String naverRedirectUrl = "/auth/loginNaver";
	private String naverClientSecret = "UpPHwRHoFv";
	
	// kakao
	private String kakaoClientId = "e3722962b6921f10e51dc9d4f5488151";
	private String kakaoRedirectUrl = "/auth/loginKakao";
	private String kakaoScope = "account_email";
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping(value = "/auth/loginNaver", method = RequestMethod.GET)
	public String loginNaver(@RequestParam(value = "code", required = false) String code,
			Model model, RedirectAttributes rattrs, HttpSession session , HttpServletRequest request) {

		System.out.println("code : " + code);
        String access_Token = getAccessToken(code, "naver", request.getParameter("state"));
        System.out.println("access_Token : " + access_Token);
        
        
        HashMap<String, Object> userInfo = getUserInfo(access_Token,"naver");
        System.out.println("access_Token : " + access_Token);
        System.out.println("userInfo : " + userInfo.get("userEmail"));
        System.out.println("userId : " + userInfo.get("userId"));
        
        // 만약 id가 저장되어있지 않아있으면 네이버회원가입창으로 넘어감 
        Optional<UserVO> user = loginService.checkSocialLogin(userInfo.get("userId").toString());
        if(!user.isPresent()) {
        	rattrs.addFlashAttribute("userInfo",userInfo);
        	return "redirect:/register?method=naver";
        }
       
//        JSONObject kakaoInfo =  new JSONObject(userInfo);
//        model.addAttribute("kakaoInfo", kakaoInfo);
        session.setAttribute("user", (UserVO)user.get());

        return "redirect:/";
	}
	
	// 카카오 로그인 
	@RequestMapping(value = "/auth/loginKakao", method = RequestMethod.GET)
	public String loginKaKao(@RequestParam(value = "code", required = false) String code, Model model, RedirectAttributes rattrs, HttpSession session) throws Exception {

		System.out.println("code : " + code);
        String access_Token = getAccessToken(code, "kakao", "");
        System.out.println("access_Token : " + access_Token);
        
        
        HashMap<String, Object> userInfo = getUserInfo(access_Token,"kakao");
        System.out.println("access_Token : " + access_Token);
        System.out.println("userInfo : " + userInfo.get("email"));
        System.out.println("userId : " + userInfo.get("id"));
        
        // 만약 id가 저장되어있지 않아있으면 카카오회원가입창으로 넘어감 
        Optional<UserVO> user = loginService.checkSocialLogin(userInfo.get("id").toString());
        if(!user.isPresent()) {
        	rattrs.addFlashAttribute("userInfo",userInfo);
        	return "redirect:/register?method=kakao";
        }
       
//        JSONObject kakaoInfo =  new JSONObject(userInfo);
//        model.addAttribute("kakaoInfo", kakaoInfo);
        session.setAttribute("user", (UserVO)user.get());

        return "redirect:/";
	}

	
	// 로그인 - 로그인 창
	@RequestMapping(value = "/auth/login", method = RequestMethod.GET)
	public String loginForm(HttpSession session, Model model) {
		
		// naver
		SecureRandom random = new SecureRandom();
	    String state = new BigInteger(130, random).toString(); // 네이버는 별도의 난수가 필요하다 
	    
	    String naverApiUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code"
	    					+ "&client_id=" + naverClientId
	    					+ "&redirect_uri=" + domain_url + naverRedirectUrl
	    					+ "&state=" + state;
	    session.setAttribute("naverState", state);
	    
	    model.addAttribute("naverApiUrl",naverApiUrl);
	    
	    // kakao
	    String kakaoApiUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
	    					+ "&client_id=" + kakaoClientId
	    					+ "&redirect_uri=" + domain_url + kakaoRedirectUrl
	    					+ "&scope=" + kakaoScope;
	    
	    model.addAttribute("kakaoApiUrl",kakaoApiUrl);
	    
		return "user/auth/login";
	}
	
	// 로그인 - 로그인 실행 
	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	public String login(String userId, String userPw, HttpSession session) {
		// 로그인 성공시 
		Optional<UserVO> user = loginService.checkLogin(userId, userPw);
		System.out.println(user);
		if(user.isPresent()) {
			session.setAttribute("user", (UserVO)user.get());
			return "redirect:/";
		}
		return "user/auth/login";
		
	}
	
    // 토큰 발급
	public String getAccessToken (String authorize_code, String platform, String naverState) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "";
        if(platform.equals("kakao")) reqURL = "https://kauth.kakao.com/oauth/token";
        else if(platform.equals("naver")) reqURL = "https://nid.naver.com/oauth2.0/token";
        
        
        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // URL연결은 입출력에 사용 될 수 있고, POST 혹은 PUT 요청을 하려면 setDoOutput을 true로 설정해야함.
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //	POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            
            if(platform.equals("kakao")) {
                sb.append("&client_id=").append(kakaoClientId); //본인이 발급받은 key
                sb.append("&redirect_uri=").append(domain_url).append(kakaoRedirectUrl);     // 본인이 설정해 놓은 경로
            }
            else if(platform.equals("naver")) {
                sb.append("&client_id=").append(naverClientId); //본인이 발급받은 key
                sb.append("&redirect_uri=").append(domain_url).append(naverRedirectUrl);     // 본인이 설정해 놓은 경로
                sb.append("&client_secret=").append(naverClientSecret); //본인이 발급받은 secret key
                sb.append("&state=").append(naverState);  
                
            }
            
            sb.append("&code=" + authorize_code);
            System.out.println("code : " +sb.toString());
            bw.write(sb.toString());
            bw.flush();

            
            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;
    }
	
    //유저정보조회
    public HashMap<String, Object> getUserInfo (String access_Token, String platform) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        
        String reqURL = "";
        if(platform.equals("kakao")) reqURL = "https://kapi.kakao.com/v2/user/me";
        else if(platform.equals("naver")) reqURL = "https://openapi.naver.com/v1/nid/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
   

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            if(platform.equals("kakao")) {
                JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
                String id = element.getAsJsonObject().get("id").getAsString();
                String email = kakao_account.getAsJsonObject().get("email").getAsString();
                
                userInfo.put("id", id);
                userInfo.put("accessToken", access_Token);
                userInfo.put("email", email);
            }
            else if(platform.equals("naver")) {
                JsonObject naver_account = element.getAsJsonObject().get("response").getAsJsonObject();
                String id = naver_account.getAsJsonObject().get("id").getAsString();
                String email = naver_account.getAsJsonObject().get("email").getAsString();
                String name = naver_account.getAsJsonObject().get("name").getAsString();
                String mobile = naver_account.getAsJsonObject().get("mobile").getAsString();
                String birthday = naver_account.getAsJsonObject().get("birthday").getAsString();
                String birthyear = naver_account.getAsJsonObject().get("birthyear").getAsString();
                
                userInfo.put("userId", id);
                userInfo.put("accessToken", access_Token);
                userInfo.put("userEmail", email);
                userInfo.put("userName", name);
                userInfo.put("userPhone", mobile);
                userInfo.put("birthday", birthday);
                userInfo.put("birthyear", birthyear);
                
                userInfo.put("userBirth",  birthyear + "-" + birthday);
                
                String[] userPhoneList = mobile.split("-");
                
                userInfo.put("userPhone1", userPhoneList[0]);
                userInfo.put("userPhone2", userPhoneList[1]);
                userInfo.put("userPhone3", userPhoneList[2]);
                
                System.out.println(name);
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }
 
}
