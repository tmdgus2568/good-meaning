package com.goodmeaning.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.admin.AdminProductRepository;
import com.goodmeaning.vo.PageVO;
import com.querydsl.core.types.Predicate;

@Service
public class AdminStockService {

	@Autowired
	private AdminProductRepository productRepo;

	

	// 재고 리스트 가져오기
	public Page<Object[]> stockList(PageVO pageVO, int direction, String colmnName) {

		Pageable paging = pageVO.makePaging(direction, colmnName); // 전체 order

		Predicate pre = productRepo.makePredicate(pageVO); // 조건넣기
		Page<Object[]> result = productRepo.findProductAll(paging); //pre, 

		return result;
	}

	public Long selectAll(PageVO pageVO) {
		Predicate pre = productRepo.makePredicate(pageVO);
		return productRepo.count(pre);
	}

}
