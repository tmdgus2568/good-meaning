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
	@Query(value = "select product_mainimg1, product_name, cart_count, product_price, cart_count*product_price sumMoney, cart_num"
			+ " from tbl_user u, tbl_product p, tbl_cart c"
			+ " where u.user_phone=c.user_phone and p.product_num=c.product_num"
			+ " and c.user_phone=?1", nativeQuery = true)
	/* + "	and c.cart_num=?2" */
	public List<Object[]> findAllCartList(String userPhone);

}
