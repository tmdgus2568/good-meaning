package com.goodmeaning;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.UserRole;
import com.goodmeaning.vo.UserVO;

@SpringBootTest
public class test1 {

	@Autowired
	OrderRepository orepo;
	
	@Autowired
	UserRepository urepo;
	
	//@Test
	public void insert() {
	
		Date to = Date.valueOf("2012-03-15");
		
		UserVO user = UserVO.builder()
				.user_phone("123")
				.user_id("abc")
				.user_pw("11")
				.user_email("a@naver.com")
				.user_name("홍길동")
				.user_postcode("888")
				.user_address("서울")
				.user_address_detail("강남구")
				.join_platform("일반")
				.user_birth(to)
				.user_role(UserRole.USER)
				.build();
		
		urepo.save(user);
	}
}
