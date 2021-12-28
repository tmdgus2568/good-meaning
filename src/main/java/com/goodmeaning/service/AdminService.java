package com.goodmeaning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.HJRepository;
import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.querydsl.core.types.Predicate;

@Service
public class AdminService {

	@Autowired
	private HJRepository repo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ProductOptionRepository productOptionRepo;

//	// 재고리스트 보여주기
//	public Page<ProductVO> stockList(PageVO pageVO) {
//		
//		Pageable paging = pageVO.makePaging(0, null); //전체 order
//		
//		Predicate pre = repo.makePredicate(pageVO.getType(), pageVO.getKeyword()); // 조건넣기
//		Page<ProductVO> result = repo.findAll(pre, paging);
//		
//		return result; 
//	}
	///select count() from aaa where 절 

	// 상품리스트 보여주기
	public Page<ProductVO> productList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order

		Predicate pre = repo.makePredicate(pageVO); // 조건넣기
		Page<ProductVO> result = repo.findAll(pre, paging);

		return result;
	}

	 
	// 상품등록
	public ProductVO insertProduct(ProductVO product, String[] optionName, int[] optionPrice, String optionCategory) {
		ProductVO newproduct = repo.save(product); // 객체생성후 저장
		System.out.println(newproduct);
		for (int i = 0; i < optionName.length; i++) {
			ProductOptionVO option = new ProductOptionVO(); // 객체 생성 후 저장
			option.setOptionName(optionName[i]);
			option.setExtraprice(optionPrice[i]);
			option.setOptionCategory(optionCategory);
			option.setProductNum(newproduct);
			System.out.println(option);
			productOptionRepo.save(option);
		}

		return newproduct;
	}

	// 상품딕테일
	public ProductOptionVO findByProductNum(ProductVO product) {
		return productOptionRepo.findByProductNum(product);
	}

	// 재고 리스트 가져오기
	public Page<Object[]> stockList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order

		Predicate pre = repo.makePredicate(pageVO); // 조건넣기
		Page<Object[]> result = repo.findProductAll(pre, paging);

		return result;
	}

	public Long selectAll() {
		// TODO Auto-generated method stub
		return productRepo.count();
	}

}
