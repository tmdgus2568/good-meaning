package com.goodmeaning.security;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserVO;


@Service
public class UserService implements UserDetailsService{
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	PasswordEncoder passwordEncoder; // security config에서 Bean생성
	// 회원가입
	@Transactional
	public UserVO joinUser(UserVO user) {
		// 비밀번호 암호화...암호화되지않으면 로그인되지않는다.
		String pwd = passwordEncoder.encode(user.getUserPw());
		user.setUserPw(pwd);
		System.out.println(pwd);
		return userRepo.save(user);
	}

	//반드시 구현해야한다. 
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername mid:" + username);
		//filter는 조건에 맞는것만 선택
		//map: 변형한다. 
	 
		Optional<UserVO> user = userRepo.findByUserId(username);
		UserDetails userSecu = userRepo.findByUserId(username)
				.filter(u -> u != null).map(u -> new SecurityUser(u)).get();
		System.out.println("user:" + userSecu);
		httpSession.setAttribute("userSecu", userSecu);
		if(user.isPresent()) httpSession.setAttribute("user", user.get());
		return userSecu;
	}

	 
	
}