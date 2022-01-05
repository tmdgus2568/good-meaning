package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.QProductVO;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.BooleanBuilder;

public interface ProductRepository extends CrudRepository<ProductVO, Long>, QuerydslPredicateExecutor<ProductVO> {
	Optional<ProductVO> findByProductName(String pname);
	
	// 1.default: findAll(), findById(id값)
	// 2.pattern에 맞는 함수 정의: findBy속성()....
	List<ProductVO> findAll(Sort aa);
	// 3.JPQL: @Query, nativeQuery=true
	// 4.Querydsl 동적 SQL 문장 생성 가능

	public default Predicate makePredicate(String[] type, Object[] keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		QProductVO product = QProductVO.productVO;
		builder.and(product.productNum.gt(0));
		// 검색조건처리
		if (type == null || keyword.length == 0)
			return builder;
		switch (type[0]) {
		// and title like '%??%'
		case "name":
			builder.and(product.productName.like("%" + (String)keyword[0] + "%"));
			break;
		default:
			break;
		}
		return builder;
	}
}