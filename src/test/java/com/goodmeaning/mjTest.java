package com.goodmeaning;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.OrderRepository;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.ReviewAnswerRepository;
import com.goodmeaning.persistence.ReviewRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.vo.Category;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewAnswerVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserRole;
import com.goodmeaning.vo.UserVO;

@SpringBootTest
public class mjTest {

	@Autowired
	OrderRepository orepo;
	
	@Autowired
	OrderDetailRepository odrepo;

	@Autowired
	UserRepository urepo;

	@Autowired
	ProductRepository prepo;

	@Autowired
	ProductOptionRepository oprepo;
	

	@Autowired
	ReviewRepository rrepo;
	
	@Autowired
	ReviewAnswerRepository rarepo;
	
	
	private static String URL = "https://thepicker.net";

	// @Test
	public void insert() {

		Date to = Date.valueOf("2012-03-15");

		UserVO user = UserVO.builder().userPhone("123").userId("abc").userPw("11").userEmail("a@naver.com")
				.userName("홍길동").userPostcode("888").userAddress("서울").userAddressDetail("강남구").joinPlatform("일반")
				.userBirth(to).userRole(UserRole.USER).build();

		urepo.save(user);
	}

	// @Test
	public void find() {

		prepo.findAll().forEach(a -> {
			System.out.println(a);
		});
	}

	// @Test
	public void insertAll() throws IOException {

		Category[] c = Category.values();
		for (Category category : c) {

			Document doc = Jsoup.connect(URL + "/" + category.toString()).get();

			Elements namecontents = doc.select(".item-pay ._fade_link"); // 상품명
			Elements pricecontents = doc.select(".item-pay .inline-blocked"); // 상품가격
			Elements productdetailpage = doc
					.select("[class~=(_fade_link shop-item-thumb|_fade_link shop-item-thumb hover_img_none)]");

			for (int i = 0; i < namecontents.size(); i++) {

				Set<Category> cate = new HashSet<Category>();
				cate.add(category);

				ProductVO pro = new ProductVO();

				Element name = namecontents.get(i);
				Element price = pricecontents.get(i);

				String nameStrBefore = name.text();
				int idx = nameStrBefore.indexOf("]");
				String nameStrAfter = nameStrBefore.substring(idx + 2); // 상품명
				int priceInt = Integer.parseInt((price.text().replace(",", "")).replace("원", "")); // 가격

				pro.setProductName(nameStrAfter);
				pro.setProductPrice(priceInt);
				pro.setProductCategory(cate);

				// 각 상품별 디페일 페이지로 이동을 위한 URL 추출
				String detailurltail = productdetailpage.get(i).attr("href");
				String DETAILURL = URL + detailurltail; // 디테일 URL 합성

				Document doc2 = Jsoup.connect(DETAILURL).referrer(DETAILURL).userAgent("chrome").get();

				Elements maincontents = doc2.select(".owl-dot a");
				Elements detailcontents = doc2.getElementsByAttributeValue("id", "prodDetailPC");
				String detailcontent = detailcontents.first().toString();
				int start = detailcontent.indexOf("data-original");
				int last = detailcontent.indexOf("\"></p>");

				String detailimg = detailcontent.substring(start + 15, last); // 디테일 이미지
				pro.setProductDetailimg(detailimg);

				List<String> mainimgs = new ArrayList<>();

				for (int j = 0; j < maincontents.size(); j++) {
					String main3 = maincontents.get(j).attr("style");
					String mainimg[] = main3.split("'");
					mainimgs.add(mainimg[1]);
				}

				System.out.println(mainimgs.size());

				if (mainimgs.size() == 1) {
					pro.setProductMainimg1(mainimgs.get(0));
				} else if (mainimgs.size() == 2) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
				} else if (mainimgs.size() == 3) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
					pro.setProductMainimg3(mainimgs.get(2));
				} else if (mainimgs.size() >= 4) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
					pro.setProductMainimg3(mainimgs.get(2));
					pro.setProductMainimg4(mainimgs.get(3));
				}

				prepo.save(pro);
			}
		}
	}

	// @Test
	public void insertAll2() throws IOException {

		List<String> proids = new ArrayList<>();
		List<String> catemap = new ArrayList<>();

		Category[] c = Category.values();
		for (Category category : c) {

			Document doc = Jsoup.connect(URL + "/" + category.toString()).referrer(URL).userAgent("chrome").get();

			Elements namecontents = doc.select(".item-pay ._fade_link"); // 상품명
			Elements pricecontents = doc.select(".item-pay .inline-blocked"); // 상품가격
			Elements productdetailpage = doc
					.select("[class~=(_fade_link shop-item-thumb|_fade_link shop-item-thumb hover_img_none)]");

			for (int i = 0; i < namecontents.size(); i++) {

				Set<Category> cate = new HashSet<Category>();
				cate.add(category);
				ProductVO pro = new ProductVO();

				Element name = namecontents.get(i);
				Element price = pricecontents.get(i);

				String nameStrBefore = name.text();
				int idx = nameStrBefore.indexOf("]");
				String nameStrAfter = nameStrBefore.substring(idx + 2); // 상품명
				int priceInt = Integer.parseInt((price.text().replace(",", "")).replace("원", "")); // 가격

				pro.setProductName(nameStrAfter);
				pro.setProductPrice(priceInt);

				// 각 상품별 디페일 페이지로 이동을 위한 URL 추출
				String detailurltail = productdetailpage.get(i).attr("href");

				int equalloc = detailurltail.indexOf("=");
				String proid = detailurltail.substring(equalloc + 1);

				for (int j = 0; j < proids.size(); j++) {
					if (proid.equals(proids.get(j))) {
						cate.add(Category.valueOf(catemap.get(j)));
					}
				}
				proids.add(proid);
				catemap.add(category.toString());

				pro.setProductCategory(cate);

				String DETAILURL = URL + detailurltail; // 디테일 URL 합성

				Document doc2 = Jsoup.connect(DETAILURL).referrer(DETAILURL).userAgent("chrome").get();

				Elements maincontents = doc2.select(".owl-dot a");
				Elements detailcontents = doc2.getElementsByAttributeValue("id", "prodDetailPC");
				String detailcontent = detailcontents.first().toString();
				int start = detailcontent.indexOf("data-original");
				int last = detailcontent.indexOf("\"></p>");

				String detailimg = detailcontent.substring(start + 15, last); // 디테일 이미지
				pro.setProductDetailimg(detailimg);

				List<String> mainimgs = new ArrayList<>();

				for (int j = 0; j < maincontents.size(); j++) {
					String main3 = maincontents.get(j).attr("style");
					String mainimg[] = main3.split("'");
					mainimgs.add(mainimg[1]);
				}

				if (mainimgs.size() == 1) {
					pro.setProductMainimg1(mainimgs.get(0));
				} else if (mainimgs.size() == 2) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
				} else if (mainimgs.size() == 3) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
					pro.setProductMainimg3(mainimgs.get(2));
				} else if (mainimgs.size() >= 4) {
					pro.setProductMainimg1(mainimgs.get(0));
					pro.setProductMainimg2(mainimgs.get(1));
					pro.setProductMainimg3(mainimgs.get(2));
					pro.setProductMainimg4(mainimgs.get(3));
				}

				prepo.save(pro);
			}
		}
	}

	// @Test
	public void insertAll3() throws IOException {

		List<String> proids = new ArrayList<>();
		List<String> catemap = new ArrayList<>();

		Category[] c = Category.values();
		for (Category category : c) {

			Document doc = Jsoup.connect(URL + "/" + category.toString()).referrer(URL).userAgent("chrome").get();

			Elements namecontents = doc.select(".item-pay ._fade_link"); // 상품명
			Elements productdetailpage = doc
					.select("[class~=(_fade_link shop-item-thumb|_fade_link shop-item-thumb hover_img_none)]");

			for (int i = 0; i < namecontents.size(); i++) {

				Set<Category> cate = new HashSet<Category>();
				cate.add(category);

				// 각 상품별 디페일 페이지로 이동을 위한 URL 추출
				String detailurltail = productdetailpage.get(i).attr("href");

				int equalloc = detailurltail.indexOf("=");
				String proid = detailurltail.substring(equalloc + 1);

				for (int j = 0; j < proids.size(); j++) {
					if (proid.equals(proids.get(j))) {
						cate.add(Category.valueOf(catemap.get(j)));
					}
				}
				proids.add(proid);
				catemap.add(category.toString());

			}
		}
	}

	// @Test
	public void enumtest() throws IOException {

		Set<Category> c = new HashSet<Category>();
		c.add(Category.BATHROOM);
		c.add(Category.valueOf("KITCHEN"));

		System.out.println(c.toString());

	}

	// tbl_product 최종본
	// @Test
	public void insertAll4() throws IOException {

		List<String> proids = new ArrayList<>();
		List<String> catemap = new ArrayList<>();

		Category[] c = Category.values();
		for (Category category : c) {

			Document doc = Jsoup.connect(URL + "/" + category.toString()).referrer(URL).userAgent("chrome").get();

			Elements namecontents = doc.select(".item-pay ._fade_link"); // 상품명
			Elements pricecontents = doc.select(".item-pay .inline-blocked"); // 상품가격
			Elements productdetailpage = doc
					.select("[class~=(_fade_link shop-item-thumb|_fade_link shop-item-thumb hover_img_none)]");

			for (int i = 0; i < namecontents.size(); i++) {

				Set<Category> cate = new HashSet<Category>();
				cate.add(category);

				ProductVO pro = new ProductVO();

				Element name = namecontents.get(i);
				Element price = pricecontents.get(i);

				String nameStrBefore = name.text();
				int idx = nameStrBefore.indexOf("]");
				String nameStrAfter = nameStrBefore.substring(idx + 2); // 상품명
				int priceInt = Integer.parseInt((price.text().replace(",", "")).replace("원", "")); // 가격

				pro.setProductName(nameStrAfter);
				pro.setProductPrice(priceInt);

				// 각 상품별 디페일 페이지로 이동을 위한 URL 추출
				String detailurltail = productdetailpage.get(i).attr("href");

				int equalloc = detailurltail.indexOf("=");
				String proid = detailurltail.substring(equalloc + 1);

				if (prepo.findByProductName(nameStrAfter).isPresent() == true) {

					ProductVO existpro = prepo.findByProductName(nameStrAfter).get();

					for (int j = 0; j < proids.size(); j++) {
						if (proid.equals(proids.get(j))) {
							cate.add(Category.valueOf(catemap.get(j)));
						}
					}

					existpro.setProductCategory(cate);

					proids.add(proid);
					catemap.add(category.toString());

					prepo.save(existpro);

				} else {

					pro.setProductCategory(cate);

					proids.add(proid);
					catemap.add(category.toString());

					String DETAILURL = URL + detailurltail; // 디테일 URL 합성

					Document doc2 = Jsoup.connect(DETAILURL).referrer(DETAILURL).userAgent("chrome").get();

					Elements maincontents = doc2.select(".owl-dot a");
					Elements detailcontents = doc2.getElementsByAttributeValue("id", "prodDetailPC");
					String detailcontent = detailcontents.first().toString();
					int start = detailcontent.indexOf("data-original");
					int last = detailcontent.indexOf("\"></p>");

					String detailimg = detailcontent.substring(start + 15, last); // 디테일 이미지
					pro.setProductDetailimg(detailimg);

					List<String> mainimgs = new ArrayList<>();

					for (int j = 0; j < maincontents.size(); j++) {
						String main3 = maincontents.get(j).attr("style");
						String mainimg[] = main3.split("'");
						mainimgs.add(mainimg[1]);
					}

					if (mainimgs.size() == 1) {
						pro.setProductMainimg1(mainimgs.get(0));
					} else if (mainimgs.size() == 2) {
						pro.setProductMainimg1(mainimgs.get(0));
						pro.setProductMainimg2(mainimgs.get(1));
					} else if (mainimgs.size() == 3) {
						pro.setProductMainimg1(mainimgs.get(0));
						pro.setProductMainimg2(mainimgs.get(1));
						pro.setProductMainimg3(mainimgs.get(2));
					} else if (mainimgs.size() >= 4) {
						pro.setProductMainimg1(mainimgs.get(0));
						pro.setProductMainimg2(mainimgs.get(1));
						pro.setProductMainimg3(mainimgs.get(2));
						pro.setProductMainimg4(mainimgs.get(3));
					}

					prepo.save(pro);
				}
			}
		}
	}

	//@Test
	public void selectAll() {
		prepo.findAll().forEach(a -> {
			System.out.println(a.toString());
		});
	}

	////////////////////////

	
	//@Test
		public void insertOption5() {

			ProductVO pro = prepo.findByProductName("피커북 떡메모지").get();

			ProductOptionVO op = ProductOptionVO.builder().optionName("클레이래드").optionStock(50).productNum(pro).build();
			ProductOptionVO op1 = ProductOptionVO.builder().optionName("샌드브릭").optionStock(50).productNum(pro).build();

			oprepo.save(op);
			oprepo.save(op1);
		}
	
	//@Test
		public void insertOption4() {

			ProductVO pro = prepo.findByProductName("유기농 메쉬 프로듀스백(S/M/L)").get();

			ProductOptionVO op = ProductOptionVO.builder().optionName("small").optionStock(50).productNum(pro).build();

			ProductOptionVO op1 = ProductOptionVO.builder().optionName("medium").optionStock(50).extraprice(1000)
					.productNum(pro).build();

			ProductOptionVO op2 = ProductOptionVO.builder().optionName("large").optionStock(50).extraprice(1000)
					.productNum(pro).build();

			ProductOptionVO op3 = ProductOptionVO.builder().optionName("3종세트").optionStock(50).extraprice(9500)
					.productNum(pro).build();

			oprepo.save(op);
			oprepo.save(op1);
			oprepo.save(op2);
			oprepo.save(op3);
		}
	
	
	
	//@Test
	public void insertOption3() {

		ProductVO pro = prepo.findByProductName("유기농면 프로듀스백(S/M/L)").get();

		ProductOptionVO op = ProductOptionVO.builder().optionName("small").optionStock(50).productNum(pro).build();

		ProductOptionVO op1 = ProductOptionVO.builder().optionName("medium").optionStock(50).extraprice(1000)
				.productNum(pro).build();

		ProductOptionVO op2 = ProductOptionVO.builder().optionName("large").optionStock(50).extraprice(1500)
				.productNum(pro).build();

		ProductOptionVO op3 = ProductOptionVO.builder().optionName("3종세트").optionStock(50).extraprice(10000)
				.productNum(pro).build();

		oprepo.save(op);
		oprepo.save(op1);
		oprepo.save(op2);
		oprepo.save(op3);
	}

	// @Test
	public void insertOption2() {

		ProductVO pro = prepo.findByProductName("2022년 친환경 기부 다이어리").get();

		ProductOptionVO op = ProductOptionVO.builder().optionName("블랙 커버").optionStock(50).productNum(pro).build();

		ProductOptionVO op1 = ProductOptionVO.builder().optionName("크라프트 커버").optionStock(50).productNum(pro).build();

		oprepo.save(op);
		oprepo.save(op1);
	}

	// @Test
	public void insertOption1() {

		ProductVO pro = prepo.findByProductName("린넨 양면 숄더백").get();

		ProductOptionVO op = ProductOptionVO.builder().optionName("브라운+웨일블루").optionStock(50).productNum(pro).build();

		oprepo.save(op);
	}
	
	//@Test
	public void insertReview() {

		ProductVO pro = prepo.findById(810L).get();
		UserVO user = urepo.findById("01011111111").get();
		System.out.println(user);
		OrderDetailVO orderdetail = odrepo.findById(898L).get();

		ReviewVO r = ReviewVO.builder()
				.reviewTitle("최고에유")
				.reviewContent("튼튼해요^^")
				.reviewLike(1)
				.userPhone(user)
				.orderDetail(orderdetail)
				.build();
		
		rrepo.save(r);			
	}
	
	
	@Test
		public void insertReviewAnswer() {

			UserVO user = urepo.findById("01011111111").get();
			ReviewVO review = rrepo.findById(943L).get();

			ReviewAnswerVO r = ReviewAnswerVO.builder()
					.ranswerContent("멋져요")
					.userPhone(user)
					.reviewNum(review)
					.build();
			
			
			rarepo.save(r);			
		}
}
