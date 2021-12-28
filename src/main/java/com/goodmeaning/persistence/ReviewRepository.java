package com.goodmeaning.persistence;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;


import com.goodmeaning.vo.QReviewVO;
import com.goodmeaning.vo.ReviewVO;
import com.goodmeaning.vo.UserVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface ReviewRepository extends CrudRepository<ReviewVO, Long>,QuerydslPredicateExecutor<ReviewVO>{

	List<ReviewVO> findByProductNum(ProductVO product);
	List<ReviewVO> findByUserPhone(UserVO userPhone);
	List<ReviewVO> findAll(Sort s);
	
	public default Predicate makePredicate(String[] type, Object[] keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		QReviewVO review = QReviewVO.reviewVO;
		builder.and(review.reviewNum.gt(0)); //select......where bno>0 
		// 검색조건처리
		if(type == null || type.length == 0) {
			return builder;
		}

		// user가 order한 것들을 가져오기 때문에 
		// where usePhone=? 조건문 사용 
		
		builder.and(review.userPhone.eq((UserVO)keyword[0]));
		
		return builder;
	}
}
