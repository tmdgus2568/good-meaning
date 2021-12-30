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

	// 상품모두(옵션따로)가져오기
	@Query(value = "select p.product_num, p.product_name, p.product_price, p.product_stock, t.option_name, t.option_stock, "
			+ "t.extraprice, c.purchase_quantity, o.order_status, sum(d.order_detail_quantity), DATE_FORMAT(c.purchase_date, \"%Y-%m-%d\") "
			+ "from tbl_product p "
			+ "left outer join tbl_product_option t on (p.product_num = t.product_num) "
			+ "left outer join tbl_purchase c on (p.product_num = c.product_num) "
			+ "left outer join tbl_order_detail d on (p.product_num = d.product_num) "
			+ "left outer join tbl_order o on (d.order_num = o.order_num) "
			+ "group by p.product_num, p.product_name, p.product_price, p.product_stock, t.option_name, t.option_stock, "
			+ "t.extraprice, c.purchase_quantity, o.order_status", 
			countQuery = "select p.product_num, p.product_name, p.product_price, p.product_stock, t.option_name, t.option_stock, "
					+ "t.extraprice, c.purchase_quantity, o.order_status, sum(d.order_detail_quantity), DATE_FORMAT(c.purchase_date, \"%Y-%m-%d\") "
					+ "from tbl_product p "
					+ "left outer join tbl_product_option t on (p.product_num = t.product_num) "
					+ "left outer join tbl_purchase c on (p.product_num = c.product_num) "
					+ "left outer join tbl_order_detail d on (p.product_num = d.product_num) "
					+ "left outer join tbl_order o on (d.order_num = o.order_num) "
					+ "group by p.product_num, p.product_name, p.product_price, p.product_stock, t.option_name, t.option_stock, "
					+ "t.extraprice, c.purchase_quantity, o.order_status", nativeQuery = true)
	public Page<Object[]> findProductAll(Pageable paging); //nativequery는 predicate 쓸 수 없음. querydsl이 entity를 사용하는 것

	
//	  select p.product_num, p.product_name, p.product_price, p.product_stock,
//	  t.option_name, t.option_stock, " +
//	  "t.extraprice, c.purchase_quantity, o.order_status, sum(d.order_detail_quantity), DATE_FORMAT(c.purchase_date, '%Y-%m-%d') "
//	  + "from tbl_product p " +
//	  "left outer join tbl_product_option t on (p.product_num = t.product_num) " +
//	  "left outer join tbl_purchase c on (p.product_num = c.product_num) " +
//	  "left outer join tbl_order_detail d on (p.product_num = d.product_num) " +
//	  "left outer join tbl_order o on (d.order_num = o.order_num) " +
//	  "group by p.product_num, p.product_name, p.product_price, p.product_stock, t.option_name, t.option_stock, "
//	  + "t.extraprice, c.purchase_quantity, o.order_status
	 

	// 상품검색(Querydsl) //재고검색
	public default Predicate makePredicate(PageVO pageVO) {
		// String sdate, String edate, String postingState ) { //기간검색하려면 keword 12개
		// 넣어야하나..?
		// null이 있을때?

		String[] keys = pageVO.getType(); // key(type)은 고정
		Object[] objects = null;

		BooleanBuilder builder = new BooleanBuilder();
		QProductVO product = QProductVO.productVO;

		Timestamp from = null, to = null;
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// 검색조건처리
		if (pageVO.getKeyword() == null)
			return builder; // 처음에 페이지 들어왔을 때만 사용

		objects = pageVO.getKeyword();

		if (objects[2] != null && objects[3] != null && !((String) objects[2]).equals("")
				&& !((String) objects[3]).equals("")) {
			LocalDateTime from_localDate = LocalDateTime.parse((String) objects[2] + " 00:00:00", formatDateTime);
			LocalDateTime to_localDate = LocalDateTime.parse((String) objects[3] + " 24:00:00", formatDateTime);
			from = Timestamp.valueOf(from_localDate);
			to = Timestamp.valueOf(to_localDate);
		}

		if (objects[0] != null && !((String) objects[0]).equals(""))
			builder.and(product.productName.like("%" + (String) objects[0] + "%"));
		if (objects[1] != null && !((String) objects[1]).equals(""))
			builder.and(product.productNum.eq(Long.valueOf((String) objects[1])));
		if (objects[2] != null && !((String) objects[2]).equals(""))
			builder.and(product.productCreatedate.between(from, to));
		System.out.println(objects[4]);
		if (objects[4] != null && !((String) objects[4]).equals("") && !(((String) objects[4]).equals("전체")))
			builder.and(product.postingState.eq((String) objects[4]));

		return builder;
	}

}
