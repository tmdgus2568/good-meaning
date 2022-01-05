package com.goodmeaning.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodmeaning.persistence.PurchaseRepository;
import com.goodmeaning.persistence.admin.AdminProductOptionRepository;
import com.goodmeaning.persistence.admin.AdminProductRepository;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.PurchaseVO;
import com.querydsl.core.types.Predicate;

@Service
public class AdminStockService {

	@Autowired
	private AdminProductRepository productRepo;

	@Autowired
	private AdminProductOptionRepository productOptionRepo;
	
	@Autowired
	private PurchaseRepository purchaseRepo;
	
	

	// 재고 리스트 가져오기
	public Page<Object[]> stockList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order

		//Predicate pre = productRepo.makeStockPredicate(pageVO); // 조건넣기
		Page<Object[]> result = productRepo.findStockAll(paging); //pre, 

		return result;
	}

	// count 함수
	/*
	 * public Long selectStockAll(PageVO pageVO) { Predicate pre =
	 * productRepo.makeStockPredicate(pageVO); return
	 * productRepo.countStockAll(pre); }
	 */

	public ProductVO findByProductNum(Long productNum) {
		return productRepo.findById(productNum).orElse(null);
	}

	public ProductOptionVO findByOptionNum(Long optionNum) {
		return productOptionRepo.findById(optionNum).orElse(null);
	}

	
	@Transactional //여러개 저장
	public String insertUpdate(Long productNum, Long optionNum, int purchaseQuantity) {
		PurchaseVO purchaseList = new PurchaseVO();
		
		ProductVO product= productRepo.findById(productNum).get();
		purchaseList.setProductNum(product);
		
		ProductOptionVO option = null;
		System.out.println("option기존거가져오기잉 : " + option);
		if(optionNum!=null && optionNum!=0L) {
			option	= productOptionRepo.findById(optionNum).get();	
			System.out.println("입고등록 널아닐때~ option = " + option);
			purchaseList.setProductOption(option);
			option.setOptionStock(option.getOptionStock() + purchaseQuantity);
			productOptionRepo.save(option);
		} else {
			System.out.println("입고등록 널일때~ option = " + option);
			product.setProductStock(product.getProductStock() + purchaseQuantity);
			productRepo.save(product);
		}
		
		purchaseList.setPurchaseQuantity(purchaseQuantity);
       
		purchaseRepo.save(purchaseList);
		
		return purchaseQuantity + "건 등록하였습니다.";
	}

}
