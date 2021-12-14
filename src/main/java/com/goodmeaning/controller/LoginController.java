package com.goodmeaning.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

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
import com.twilio.rest.messaging.v1.Session;

/*
 * 카카오 로그인 로직 : 
 * 	1. 카카오 로그인 api로 연결(a 태그로)
 *  2. 로그인에 성공하면 일회성 인증 코드를 부여해준다 
 *  3. 그 일회성 코드를 "https://kauth.kakao.com/oauth/token"을 통해 넘겨주면 
 *  	값들을 json형태로 받을 수 있다(파싱하여 정보를 가져온다)
 * */
@Controller
public class LoginController {
	@Autowired
	LoginService loginService;
	
	// 카카오 로그인 
	@RequestMapping(value = "/auth/loginKakao", method = RequestMethod.GET)
	public String loginKaKao(@RequestParam(value = "code", required = false) String code, Model model, RedirectAttributes rattrs, HttpSession session) throws Exception {

		System.out.println("code : " + code);
        String access_Token = getAccessToken(code);
        System.out.println("access_Token : " + access_Token);
        
        
        HashMap<String, Object> userInfo = getUserInfo(access_Token);
        System.out.println("access_Token : " + access_Token);
        System.out.println("userInfo : " + userInfo.get("email"));
        System.out.println("userId : " + userInfo.get("id"));
        
        // 만약 id가 저장되어있지 않아있으면 카카오회원가입창으로 넘어감 
        Optional<UserVO> user = loginService.checkKakaoLogin(userInfo.get("id").toString());
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
	public String loginForm() {
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
	public String getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

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
            sb.append("&client_id=e3722962b6921f10e51dc9d4f5488151");  //본인이 발급받은 key
            sb.append("&redirect_uri=http://localhost:8888/auth/loginKakao");     // 본인이 설정해 놓은 경로
            sb.append("&code=" + authorize_code);
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
    public HashMap<String, Object> getUserInfo (String access_Token) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

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

            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
            String id = element.getAsJsonObject().get("id").getAsString();
            String email = kakao_account.getAsJsonObject().get("email").getAsString();
            
            userInfo.put("id", id);
            userInfo.put("accessToken", access_Token);
            userInfo.put("email", email);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }
 
}
