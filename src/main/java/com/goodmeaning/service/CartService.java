package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.CartRepository;
import com.goodmeaning.vo.CartVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
	@Autowired
	CartRepository crepo; //repo=dao
	
	//장바구니 신규추가&수정
    public void insertOrUpdate(CartVO vo) {
    	crepo.save(vo);
    }
    
    //상품 있는지 조회
    public CartVO findByProductNumAndUserPhone(ProductVO productNum, 
    		UserVO userPhone){
    	return crepo.findByProductNumAndUserPhone(productNum, userPhone).orElse(null);
    }
    
    //상품 있는지 조회 (옵션있을 때)
    public CartVO findByProductNumAndUserPhoneAndProductOption(ProductVO productNum, 
    		UserVO userPhone, ProductOptionVO productOption){
    	return crepo.findByProductNumAndUserPhoneAndProductOption(productNum, userPhone, productOption).orElse(null);
    }


    //장바구니 삭제    
    public void delete(Long cartNum) {
        crepo.deleteById(cartNum);
    }  
    
    //수량 수정
    public void updateCartItemCount(Long cartNum, int count) {
    	CartVO cart = crepo.findById(cartNum).get();
		cart.setCartCount(count);
		
		CartVO result = crepo.save(cart);//변경된 수량 DB에 저장
	}
    
    //카트 조회
    public CartVO findById(Long cartNum) {
    	return crepo.findById(cartNum).orElse(null);
    }
    
    //장바구니 상품 확인
    public int countCart(Long productNum, String userPhone) {
        return crepo.countCart(productNum, userPhone);
    }
    
    //장바구니 list 보여주는
	public List<Object[]> findAllCartList(String userPhone) {
		return crepo.findAllCartList(userPhone);
	}
	
	

}
