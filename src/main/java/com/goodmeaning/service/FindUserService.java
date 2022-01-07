package com.goodmeaning.service;

import java.util.Optional;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;

@Service
public class FindUserService {
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	private static String hostSMTP;//네이버 이용시 smtp.naver.com
	

	private static String hostSMTPid;
	

	private static String hostSMTPpwd;
	
	public FindUserService(@Value("${spring.mail.host}") String hostSMTP,
			@Value("${spring.mail.username}") String hostSMTPid,
			@Value("${spring.mail.password}") String hostSMTPpwd) {
		this.hostSMTP = hostSMTP;
		this.hostSMTPid = hostSMTPid;
		this.hostSMTPpwd = hostSMTPpwd;
	}
	
	// 주어진 정보로 아이디 찾기
	public String findId(String userPhone, String userName, String userEmail) {
		Optional<UserVO> user = userRepo.findByUserPhoneAndUserNameAndUserEmail(userPhone, userName, userEmail);
		if(user.isPresent()) {
			return user.get().getUserId();
		}
		return "";
	}
	
	public void sendEmail(UserVO user, String pw) throws Exception {
		// Mail Server 설정
		String charSet = "utf-8";

		// 보내는 사람 EMail, 제목, 내용
		String fromEmail = hostSMTPid;
		String fromName = "굿미닝";
		String subject = "";
		String msg = "";

		subject = "굿미닝 임시 비밀번호 입니다.";
		msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
		msg += "<h3 style='color: blue;'>";
		msg += user.getUserName() + "님의 임시 비밀번호 입니다. 비밀번호를 변경하여 사용하세요.</h3>";
		msg += "<p>임시 비밀번호 : ";
		msg += pw + "</p></div>";

		// 받는 사람 E-Mail 주소
		String mail = user.getUserEmail();
		try {
			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setCharset(charSet);
			email.setSSL(true);
			email.setHostName(hostSMTP);
			email.setSmtpPort(587); //네이버 이용시 587

			email.setAuthentication(hostSMTPid, hostSMTPpwd);
			email.setTLS(true);
			email.addTo(mail, charSet);
			email.setFrom(fromEmail, fromName, charSet);
			email.setSubject(subject);
			email.setHtmlMsg(msg);
			email.send();
		} catch (Exception e) {
			System.out.println("메일발송 실패 : " + e);
		}
	}
	
	public String findPw(String userPhone, String userName, String userEmail, String userId) throws Exception {
		Optional<UserVO> user = userRepo.findByUserPhoneAndUserNameAndUserEmailAndUserId(userPhone, userName, userEmail, userId);
		if(user.isPresent()) {
			// 임시 비밀번호 생성
			String pw = "";
			for (int i = 0; i < 12; i++) {
				pw += (char) ((Math.random() * 26) + 97);
			}
			user.get().setUserPw(passwordEncoder.encode(pw)); // 비밀번호 변경 
			userRepo.save(user.get()); // 비밀번호 변경 
			
			// 비밀번호 변경 메일 발송
			sendEmail(user.get(),pw);
			return user.get().getUserEmail();
		
		}
		
		return "";
	}

}
