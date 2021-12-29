package com.goodmeaning.persistence;

import java.util.ArrayList;
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
		
		if(type.length > 1) {
			switch((String)keyword[1]) {
				// 입금완료 / 배송준비중 / 배송완료 
				case "orders":
					builder.and(order.orderStatus.in("입금완료","배송준비중","배송완료"));
					break;
					
				// 구매취소 / 반품 / 교환 
				case "updateorders":
					builder.and(order.orderStatus.in("구매취소","반품","교환"));
					break;
					
				default:
					builder.and(order.orderStatus.eq((String)keyword[1]));
					break;
					
			}
		}
//		if(type.length > 1 && !keyword[1].equals("전체")) {
//			builder.and(order.orderStatus.eq((String)keyword[1]));
//		}
		return builder;
	}
}
