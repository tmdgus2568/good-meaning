package com.goodmeaning.persistence.admin;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

public interface AdminProductRepository
		extends PagingAndSortingRepository<ProductVO, Long>, QuerydslPredicateExecutor<ProductVO> {

	static final String SQL1 = "select p.product_num, p.product_name, p.product_stock, o.option_num, o.option_name, o.option_stock, "
			+ "p.product_price, o.extraprice, "
			+ "SUM(CASE WHEN DATE_FORMAT(c.purchase_date, '%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d') THEN ifnull(c.purchase_quantity, 0) "
			+ "ELSE 0 END) purchase_quantity , ifnull(a.sales_quantity, 0), (ifnull(p.product_stock, 0) + ifnull(o.option_stock, 0)) total_stock";

	static final String SQL2 = "SELECT COUNT(*) ";
	
	static final String SQL3 = "from tbl_product p "
			+ "left outer join tbl_product_option o on (p.product_num = o.product_num) "
			+ "left outer join tbl_purchase c on "
			+ "case when c.option_num is null then (p.product_num = c.product_num) "
			+ "when c.option_num is not null then (o.option_num = c.option_num) end "
			+ "		left outer join "
			+ "		(select p.product_num,  o.option_num,"
			+ "		sum(case when DATE_FORMAT(r.order_date, '%Y-%m-%d') = DATE_FORMAT(now()-interval 2 day, '%Y-%m-%d') and"
			+ "        (r.order_status = '주문완료' or r.order_status = '입금완료' or r.order_status = '배송완료' or r.order_status = '배송준비중')  then ifnull(d.order_detail_quantity, 0) else 0 end)"
			+ "        - sum(case when DATE_FORMAT(r.order_date, '%Y-%m-%d') = DATE_FORMAT(now()-interval 2 day, '%Y-%m-%d') and"
			+ "        (r.order_status = '구매취소' or r.order_status = '반품') then ifnull(d.order_detail_quantity, 0) else 0 end) as sales_quantity"
			+ "		from tbl_product p"
			+ "		left outer join tbl_order_detail d on (p.product_num = d.product_num)"
			+ "		left outer join tbl_product_option o on"
			+ "		case when o.option_num is null then (p.product_num = o.product_num)"
			+ "		when o.option_num is not null then (o.option_num = d.product_option) end"
			+ "		left outer join tbl_order r on (r.order_num = d.order_num)"
			+ "		group by p.product_num, p.product_name, o.option_num, o.option_name) a  on"
			+ "        case when o.option_num is null then (p.product_num = a.product_num)"
			+ "		when o.option_num is not null then (o.option_num = a.option_num) end"
			+ "group by p.product_num, p.product_name, p.product_stock, o.option_num, o.option_name, o.option_stock,"
			+ "p.product_price, o.extraprice order by #{#paging}";
	
	// 상품모두(옵션따로)가져오기
	@Query(value = SQL1 + SQL3, countQuery = SQL2 + SQL3, nativeQuery = true)  
	public Page<Object[]> findStockAll(Pageable paging); //nativequery는 predicate 쓸 수 없음. querydsl이 entity를 사용하는 것//Predicate pre
	
	//+ " where p.product_name like ?1 and p.product_num ?2  ?3 "
	//String pname, String op, Long num,  #{#paging}
	
	/*
	 * @Query(value = SQL, countQuery = SQL, nativeQuery = true) public
	 * List<Object[]> findStockAll(Predicate pre);
	 */
	
	/*
	 * @Query(countQuery = SQL, nativeQuery = true) public Long
	 * countStockAll(Predicate pre);
	 */

	
	// 상품검색(Querydsl)
	public default Predicate makeProductPredicate(PageVO pageVO) {

		String[] keys = pageVO.getType(); // key(type)은 고정
		Object[] objects = pageVO.getKeyword();

		BooleanBuilder builder = new BooleanBuilder();
		QProductVO product = QProductVO.productVO;

		Timestamp from = null, to = null;
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// 검색조건처리
		if (pageVO.getKeyword() == null)
			return builder; // 처음에 페이지 들어왔을 때만 사용
		
				
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

	// 재고검색(Querydsl)
	/*
	 * public default Predicate makeStockPredicate(PageVO pageVO) { String[] keys =
	 * pageVO.getType(); Object[] objects = pageVO.getKeyword();
	 * 
	 * BooleanBuilder builder = new BooleanBuilder(); QProductVO product =
	 * QProductVO.productVO;
	 * 
	 * // 검색조건처리 if (pageVO.getKeyword() == null) return builder; // 처음에 페이지 들어왔을 때만
	 * 사용
	 * 
	 * if (objects[0] != null && !((String) objects[0]).equals(""))
	 * builder.and(product.productName.like("%" + (String) objects[0] + "%")); if
	 * (objects[1] != null && !((String) objects[1]).equals(""))
	 * builder.and(product.productNum.eq(Long.valueOf((String) objects[1]))); if
	 * (objects[2] != null && !((String) objects[2]).equals("") && !(((String)
	 * objects[2]).equals("전체"))) builder.and(product.postingState.eq((String)
	 * objects[2]));
	 * 
	 * return builder; }
	 */



}
