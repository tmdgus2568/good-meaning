package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.CartRepository;
import com.goodmeaning.vo.CartVO;
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

    //장바구니 삭제    
    public void delete(Long cartNum) {
        crepo.deleteById(cartNum);
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
		// TODO Auto-generated method stub
		return crepo.findAllCartList(userPhone);
	}

}
