package com.goodmeaning.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.goodmeaning.vo.CartVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;

//find할 때 조건이 있으면 repo에 써야한다. 아니면 바로 control에 가도 됨
@Repository
public interface CartRepository extends CrudRepository<CartVO, Long>{
	
	//카트 아이디와 상품을 이용해서 장바구니에 상품이 들었는지 조회
	public Optional<CartVO> findByProductNumAndUserPhone(ProductVO productNum, UserVO userPhone);	
	
    //장바구니 동일한 상품 레코드 확인
	@Query(value = "select count(*) from tbl_cart where product_Num=?1 and user_Phone=?2", nativeQuery = true)
    public int countCart(Long productNum, String userPhone);
	
	//로그인한 회원의 장바구니 전부 조회
	@Query(value = "select product_mainimg1, product_name, cart_count, product_price, product_price*cart_count money, cart_num, p.product_num, option_name"
			+ " from tbl_cart c join tbl_product p on ( c.product_num = p.product_num)"
			+ " join tbl_user u on (c.user_phone = u.user_phone)"
			+ " left outer  join tbl_product_option op on ( c.product_option = op.option_num)"
			+ " where c.user_phone=?1", nativeQuery = true)
	public List<Object[]> findAllCartList(String userPhone);
	
	//order list로 가져가는
	@Query(value = "select product_mainimg1, product_name, cart_count, product_price, cart_count*product_price sumMoney, cart_num, p.product_num, option_name, option_num"
			+ " from tbl_cart c join tbl_product p on ( c.product_num = p.product_num)"
			+ " join tbl_user u on (c.user_phone = u.user_phone)"
			+ " left outer  join tbl_product_option op on ( c.product_option = op.option_num)"
			+ " where c.cart_num in ?1", nativeQuery = true)	 
	public List<Object[]> findOrderList2(Long[] cartNum);
	
	

}
