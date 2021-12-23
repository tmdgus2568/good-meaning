package com.goodmeaning.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.QOrderVO;
import com.goodmeaning.vo.UserVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface OrderRepository extends CrudRepository<OrderVO, Long> ,QuerydslPredicateExecutor<OrderVO> {
	List<OrderVO> findByUserPhone(UserVO user);
	List<OrderVO> findAll(Sort s);
	
	public default Predicate makePredicate(String[] type, Object[] keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		QOrderVO order = QOrderVO.orderVO;
		builder.and(order.orderNum.gt(0)); //select......where bno>0 
		// 검색조건처리
		if(type == null || type.length == 0) {
			return builder;
		}

		// user가 order한 것들을 가져오기 때문에 
		// where usePhone=? 조건문 사용 
		
		builder.and(order.userPhone.eq((UserVO)keyword[0]));
//		if(type.length > 1) {
//			for(int i=1;i<type.length;i++) {
//				builder.and(order.orderStatus.in((String)keyword[i]));
//			}
//		}
		return builder;
	}
}
