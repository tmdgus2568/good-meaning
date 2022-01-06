package com.goodmeaning.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.ProductVO;


public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetailVO, Long>, QuerydslPredicateExecutor<OrderDetailVO>{
	List<OrderDetailVO> findByOrderNum(OrderVO orderNum);
	OrderDetailVO findByOrderNumAndProductNum(OrderVO orderNum, ProductVO productNum);
	
	int countByProductNum(ProductVO productNum);
	
	final String sql = "select  detail.*, ord.order_date from tbl_order ord join tbl_order_detail detail " + 
			"on (ord.order_num = detail.order_num) " + 
			"where ord.user_phone = ?2 " + 
			"and product_num = ?1 " + 
			"and  detail.order_detail_num not in  ( select order_detail " + 
			"from tbl_review  )   order by order_detail_num desc " + 
			" limit 1";
	
	final String sql2 = "select  detail.* from tbl_order ord join tbl_order_detail detail " + 
			"on (ord.order_num = detail.order_num) " + 
			"where ord.user_phone = ?2 " + 
			"and product_num = ?1 " + 
			"and  detail.order_detail_num not in  ( select order_detail " + 
			"from tbl_review  )   order by order_detail_num desc " + 
			" limit 1";
	
	@Query(value = sql, nativeQuery = true)
	public List<Object[]> selectByOrder (Long productNum, String userPhone);
	
	@Query(value = sql2, nativeQuery = true)
	public OrderDetailVO selectByOrder2 (Long productNum, String userPhone);
	
	
}
