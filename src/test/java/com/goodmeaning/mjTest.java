package com.goodmeaning;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.Category;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserRole;
import com.goodmeaning.vo.UserVO;

@SpringBootTest
public class mjTest {

	@Autowired
	OrderRepository orepo;

	@Autowired
	UserRepository urepo;

	@Autowired
	ProductRepository prepo;

	// @Test
	public void insert() {

		Date to = Date.valueOf("2012-03-15");

		UserVO user = UserVO.builder().userPhone("123")
				.userId("abc").userPw("11").userEmail("a@naver.com")
				.userName("홍길동").userPostcode("888").userAddress("서울").userAddressDetail("강남구").joinPlatform("일반")
				.userBirth(to).userRole(UserRole.USER).build();

		urepo.save(user);
	}

	//@Test
	public void find() {

		prepo.findAll().forEach(a -> {
			System.out.println(a);
		});
	}
	
	@Test
	public void insertMainImg1() throws IOException {
		String URL = "https://thepicker.net/LIVING";
		
		Document doc = Jsoup.connect(URL).get();
		Elements contents = doc.getElementsByAttributeValue("class", "_org_img org_img _lazy_img");
		Elements procontents = doc.select(".item-pay ._fade_link");
		Elements pricecontents = doc.select(".item-pay .inline-blocked");
		
		for (int i = 0; i < contents.size(); i++) {
			String img = contents.get(i).attr("src");
			Element name = procontents.get(i);
			Element price = pricecontents.get(i);
			String nameStr = name.text();
			int priceInt = Integer.parseInt((price.text().replace(",", "")).replace("원", ""));
			
			Set<Category> category = new HashSet<>();
			category.add(Category.LIVING);
			
			ProductVO pro = ProductVO.builder()
					.productName(nameStr)
					.productPrice(priceInt)
					.productCategory(category)
					.productMainimg1(img)
					.productDetailimg("")
					.build();
			
			prepo.save(pro);
		}
	}
}
