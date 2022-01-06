package com.goodmeaning.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.goodmeaning.persistence.OrderDetailRepository;
import com.goodmeaning.persistence.PurchaseRepository;
import com.goodmeaning.persistence.admin.AdminOrderRepository;
import com.goodmeaning.persistence.admin.AdminProductOptionRepository;
import com.goodmeaning.persistence.admin.AdminProductRepository;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.PurchaseVO;

@Service
public class AdminStockService {

	@Autowired
	private AdminProductRepository productRepo;

	@Autowired
	private AdminProductOptionRepository productOptionRepo;

	@Autowired
	private PurchaseRepository purchaseRepo;
	
	@Autowired
	private AdminOrderRepository orderRepo;	
	
	@Autowired
	private OrderDetailRepository orderDetailRepo;	

	// 재고 리스트 가져오기
	public Page<Object[]> stockList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order
		/*
		 * Object[] obj = pageVO.getKeyword(); String strKeyword = "%"; if (obj != null)
		 * { strKeyword = (String) obj[0]; strKeyword = "%" + strKeyword + "%"; }
		 */

		// Predicate pre = productRepo.makeStockPredicate(pageVO); // 조건넣기
		Page<Object[]> result = productRepo.findStockAll(paging);
		// pre, String colmnName, String direction
		// colmnName, direction==0?"desc":"asc"
		// strKeyword, " = ", 1169L,

		return result;
	}

	// count 함수

//	 public Long selectStockAll(PageVO pageVO) { 
//		return
//		 productRepo.countStockAll(); 
//	 }


	public ProductVO findByProductNum(Long productNum) {
		return productRepo.findById(productNum).orElse(null);
	}

	public ProductOptionVO findByOptionNum(Long optionNum) {
		return productOptionRepo.findById(optionNum).orElse(null);
	}

	//입고하기
	
	@Transactional 
	public String insertUpdate(Long productNum, Long optionNum, int purchaseQuantity) {
		//입고내역저장
		PurchaseVO purchaseList = new PurchaseVO();

		ProductVO product = productRepo.findById(productNum).get();
		purchaseList.setProductNum(product);

		ProductOptionVO option = null;
		System.out.println("option기존거가져오기잉 : " + option);
		
		if (optionNum != null && optionNum != 0L) { //옵션재고 기존+입고수
			option = productOptionRepo.findById(optionNum).get();
			purchaseList.setProductOption(option);
			option.setOptionStock(option.getOptionStock() + purchaseQuantity);
			productOptionRepo.save(option);
		} else { //상품재고 기존+입고수
			product.setProductStock(product.getProductStock() + purchaseQuantity);
			productRepo.save(product);
		}

		purchaseList.setPurchaseQuantity(purchaseQuantity);

		purchaseRepo.save(purchaseList);

		String result = purchaseList==null?"실패하였습니다😭 다시 시도해주세요." : "등록되었습니다👏";
		return result;
	}

	// 입고내역 가져오기
	public Page<PurchaseVO> historyList(PageVO pageVO, int direction, String sortColumn) {

		Pageable paging = pageVO.makePaging(direction, sortColumn); // 전체 order

		// Predicate pre = productRepo.makeStockPredicate(pageVO); // 조건넣기
		Page<PurchaseVO> result = purchaseRepo.findAll(paging);
		System.out.println("resultSS : " + result);
		
		return result;
	}
	
	public long selectHistoryAll() {
		return purchaseRepo.count();
	}

	// 출고내역 가져오기(주문내역 대체)
	public Page<OrderVO> ordersList(PageVO pageVO, int direction, String sortColumn) {
		Pageable paging = pageVO.makePaging(direction, sortColumn); // 전체 order

		Page<OrderVO> result = orderRepo.findAll(paging);
		System.out.println("resultOD : " + result);
		
		return result;
	}
	
	public long selectOrderAll() {
		return orderDetailRepo.count();
	}
}
