package com.goodmeaning;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.persistence.admin.AdminProductRepository;
import com.goodmeaning.vo.Category;
import com.goodmeaning.vo.ProductVO;

@SpringBootTest
public class HJTest {

	@Autowired
	private AdminProductRepository repo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductOptionRepository productOptionRepo;
	
	//@Test
	//service넣기
	public void insertTest() {
		Set<Category> category = new HashSet<>();
		category.add(Category.LIVING);
		category.add(Category.TRAVEL);
		
		ProductVO p = ProductVO.builder().productName("칫솔")
				.productPrice(1000).productStock(100)
				.productCategory(category)
				.productMainimg1("상품1").productDetailimg("상품1상세").build();
		repo.save(p);
		
	}

//	@Test
//	public void selectAll() {
//		repo.findAllOrderByProductCreatedateDesc().forEach(a -> {
//			System.out.println(a);
//		});
//	}
}
