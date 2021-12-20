package com.goodmeaning;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.LoginService;
import com.goodmeaning.service.MypageService;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserRole;
import com.goodmeaning.vo.UserVO;

@SpringBootTest
public class ShTest {
	@Autowired
	OrderRepository orepo;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	OrderDetailRepository odrepo;
	
	@Autowired
	ProductRepository prepo;
	
	@Autowired
    LoginService lservice;
	
	@Autowired
	MypageService mservice;
	
	
	
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
	
	//@Test
	public void loginTest() {
		System.out.println(lservice.checkLogin("abc", "11"));
	}
	
	//@Test
	public void delete() {
		urepo.deleteById("[object Object][object Object][object Object]");
	}
	
	//@Test
	public void orderInsert() {
		Optional<ProductVO> product1 = prepo.findById(790L);
		Optional<ProductVO> product2 = prepo.findById(793L);
		Optional<UserVO> user = urepo.findById("01011111111");
		OrderDetailVO detail1 = null;
		OrderDetailVO detail2 = null;
//		OrderVO order = OrderVO.builder()
//						.orderNum(1L)
//						.address("qqqq")
//						.addressDetail("123호")
//						.deiveryPhone("0101112323")
//						.deliveryRecipient("홍길동")
//						.orderStatus("배송준비중")
//						.orderTotalPrice(30000)
//						.postcode("232323")
//						.userPhone(user.get())
//						.build();
//		
//		orepo.save(order);
		
		Optional<OrderVO> order = orepo.findById(1L);
						
//		if(product1.isPresent()) {
//			detail1 = OrderDetailVO.builder()
//					.orderDetailQuantity(2)
//					.orderDetailPrice(4000*2)
//					.productNum(product1.get())
//					.orderNum(order.get())
//					.build();
//			odrepo.save(detail1);
//			
//		}

		if(product2.isPresent()) {
			detail2 = OrderDetailVO.builder()
					.orderDetailQuantity(4)
					.orderDetailPrice(5500*4)
					.productNum(product2.get())
					.orderNum(order.get())
					.build();
			
			odrepo.save(detail2);
		}
		
	}
	@Test
	public void orderDetailTest() {
//		Optional<UserVO> user = urepo.findById("01011111111");
		Optional<OrderVO> order = orepo.findById(1L);
		if(order.isPresent())
			System.out.println(order.get());
		
	}

}
