package com.goodmeaning.persistence;

 
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.QProductVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface HJRepository
		extends PagingAndSortingRepository<ProductVO, Long>, QuerydslPredicateExecutor<ProductVO> {

	//상품모두(옵션따로)가져오기
	@Query("select p, o from ProductVO p left outer join ProductOptionVO o on "
			+ "(p.productNum = o.productNum) order by p.productNum desc")
	public Page<Object[]> findProductAll(Predicate pre, Pageable paging); 

	
	//상품검색(Querydsl) //재고검색
	public default Predicate makePredicate(PageVO pageVO) {
			//String sdate, String edate, String postingState ) { //기간검색하려면 keword 12개 넣어야하나..?
		//null이 있을때?
		 
		String[] keys = pageVO.getType(); //key(type)은 고정
		Object[] objects = null;
		
		BooleanBuilder builder = new BooleanBuilder();
		QProductVO product = QProductVO.productVO;
		
		Timestamp from=null, to=null;
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		// 검색조건처리
		if (pageVO.getKeyword() == null) return builder; //처음에 페이지 들어왔을 때만 사용
		
		objects = pageVO.getKeyword();
		
		if(objects[2]!=null && objects[3]!=null && objects[2]!="" && objects[3]!="") {
			LocalDateTime from_localDate = LocalDateTime.parse((String)objects[2]+" 00:00:00", formatDateTime);
			LocalDateTime to_localDate = LocalDateTime.parse((String)objects[3]+" 24:00:00", formatDateTime);
			from = Timestamp.valueOf(from_localDate);
			to = Timestamp.valueOf(to_localDate);
		}
		
		if(objects[0]!=null && objects[0]!="") builder.and(product.productName.like("%" + (String)objects[0] + "%"));
		if(objects[1]!=null && objects[1]!="") builder.and(product.productNum.eq(Long.valueOf((String)objects[1])));
		if(objects[2]!=null && objects[2]!="") builder.and(product.productCreatedate.between(from, to));
		System.out.println(objects[4]);
		if(objects[4]!=null && objects[4]!="" && !(((String)objects[4]).equals("전체"))) builder.and(product.postingState.eq((String)objects[4]));

		return builder;
	}

	
}
