package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private HJRepository hjRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private ProductOptionRepository productOptionRepo;
	
//	@Autowired
//	private HJRepositoryOrder orderRepo;

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

		Predicate pre = hjRepo.makePredicate(pageVO); // 조건넣기
		Page<ProductVO> result = hjRepo.findAll(pre, paging);

		return result;
	}

	 
	// 상품등록
	public ProductVO insertProduct(ProductVO product, String[] optionName, int[] optionPrice, String optionCategory) {
		ProductVO newproduct = hjRepo.save(product); // 객체생성후 저장
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
	public List<ProductOptionVO> findByProductNum(ProductVO product) {
		return productOptionRepo.findByProductNum(product);
	}

	// 재고 리스트 가져오기
	public Page<Object[]> stockList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order

		Predicate pre = hjRepo.makePredicate(pageVO); // 조건넣기
		Page<Object[]> result = hjRepo.findProductAll(paging); //pre, 

		return result;
	}

	public Long selectAll(PageVO pageVO) {
		Predicate pre = hjRepo.makePredicate(pageVO);
		return productRepo.count(pre);
	}

	
	@Transactional
	public void deleteByProductNum(Long productNum) {
		productOptionRepo.deleteByProductNum(productNum);
		hjRepo.deleteById(productNum); //지우는 순서 옵션 > 프로덕트
	}

	@Transactional
	public ProductVO updateProduct(ProductVO product, String[] optionName, Integer[] optionPrice, String optionCategory,
			Long[] optionNum, int options) {
		ProductVO original = hjRepo.findById(product.getProductNum()).get();
		original.setProductCategory(product.getProductCategory());
		original.setProductName(product.getProductName());
		original.setProductPrice(product.getProductPrice());
		original.setProductDetailimg(product.getProductDetailimg());
		original.setProductMainimg1(product.getProductMainimg1());
		original.setProductMainimg2(product.getProductMainimg2());
		original.setProductMainimg3(product.getProductMainimg3());
		original.setProductMainimg4(product.getProductMainimg4());
		original.setUploadFile(product.getUploadFile());
		
		ProductVO updateProduct = hjRepo.save(original);
		 // 객체 생성 후 저장
		
		//옵션 사용함 > 사용하지 않음으로 수정 시
		if(options == 0) {
			productOptionRepo.deleteByProductNum(product.getProductNum());
		//옵션 사용함 > 사용함 옵션 수정
		} else {//옵선번호 있으면 옵션이름(있으면 수정, 없으면 삭제)/가격, 없으면 입력
			ProductOptionVO option= null;
			for(int i = 0; i < optionName.length; i++) {
				if(optionNum.length==0 || optionNum[i] == null) {
					option = new ProductOptionVO();
					option.setOptionCategory(optionCategory);
					option.setOptionName(optionName[i]);
					option.setExtraprice(optionPrice[i]);
					option.setProductNum(updateProduct);
					productOptionRepo.save(option);
				} else {
					//option.setOptionNum(optionNum[i]);
					if(optionName[i] == null || optionName[i].equals("")) {
						productOptionRepo.deleteById(optionNum[i]);
						
					} else {
						option = productOptionRepo.findById(optionNum[i]).get(); //원래 있던 것
						option.setOptionCategory(optionCategory);
						option.setOptionName(optionName[i]);
						option.setExtraprice(optionPrice[i]);
						option.setProductNum(updateProduct);
						productOptionRepo.save(option);
					}
				}
				System.out.println("update option : " + option);
			}
		}
		return updateProduct;
	}


	public ProductVO findById(Long productNum) {
		// TODO Auto-generated method stub
		return hjRepo.findById(productNum).get();
	}


	/*
	 * public Page<OrderVO> orderList(PageVO pageVO, int direction, String
	 * colmnName) { Pageable paging = pageVO.makePaging(direction, colmnName); // 전체
	 * order
	 * 
	 * Predicate pre = orderRepo.makePredicate(pageVO); // 조건넣기 Page<OrderVO> result
	 * = orderRepo.findOrderAll(paging); //pre,
	 * 
	 * return result; }
	 */

}
