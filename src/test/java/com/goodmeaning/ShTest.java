package com.goodmeaning;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.ManageUserService;
import com.goodmeaning.vo.UserRole;
import com.goodmeaning.vo.UserVO;

@SpringBootTest
public class ShTest {
	@Autowired
	OrderRepository orepo;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	ManageUserService uservice;
	
	//@Test
	public void insert() {
	
		Date to = Date.valueOf("2012-03-15");
		
		UserVO user = UserVO.builder()
				.userPhone("123")
				.userId("abc")
				.userPw("11")
				.userEmail("a@naver.com")
				.userName("홍길동")
				.userPostcode("888")
				.userAddress("서울")
				.userAddressDetail("강남구")
				.joinPlatform("일반")
				.userBirth(to)
				.userRole(UserRole.USER)
				.build();
		
		System.out.println(user);
		
		urepo.save(user);
	}
	
	@Test
	public void loginTest() {
		System.out.println(uservice.checkLogin("abc", "11"));
	}

}
