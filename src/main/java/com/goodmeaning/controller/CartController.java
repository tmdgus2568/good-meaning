package com.goodmeaning.controller;


import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.goodmeaning.persistence.ProductOptionRepository;
import com.goodmeaning.persistence.UserRepository;
import com.goodmeaning.service.CartService;
import com.goodmeaning.service.ProductService;
import com.goodmeaning.vo.CartVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.UserVO;

@Controller
public class CartController {
	
	@Autowired
	CartService cartService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	ProductOptionRepository porepo;
	
	@RequestMapping("/cartlist")//브라우저에서 요청하는 코드
	public String selectAll(HttpSession session, Model model) {
		 // session의 id
//		UserVO user = urepo.findById("01011114444").get();
//    	session.setAttribute("user", user);
    	UserVO user = (UserVO)session.getAttribute("user");    
    	
		model.addAttribute("clist", cartService.findAllCartList(user.getUserPhone()));		
		return "user/cart/cartlist";		
	}
	
	//장바구니 추가Test
	@GetMapping("/addcart")
    public String insertget(){
		return "user/cart/addcart";
	}
	
    @PostMapping("/cart")
    public String insert(CartVO vo, Long optionNum,  Long productNum, HttpSession session){
    	//로그인된 유저 확인
    	System.out.println("cartvo:" + vo);
//    	UserVO user = urepo.findById("01011114444").get();
//    	session.setAttribute("user", user );
    	UserVO user = (UserVO)session.getAttribute("user");
        vo.setUserPhone(user);
        ProductVO product = productService.selectById(productNum);       
        vo.setProductNum(product);
        System.out.println("optionNum=" + optionNum);
        if(optionNum!=null && optionNum!=0)
             vo.setProductOption(porepo.findById(optionNum).get());
        // 장바구니에 기존 상품이 있는지 검사
        int count = cartService.countCart(productNum, user.getUserPhone());
       
        if(count == 0){
            // 없으면 insert
            cartService.insertOrUpdate(vo);
        } else {
            // 있으면 update
        	CartVO existCart = cartService.findByProductNumAndUserPhone(product, user);
        	existCart.setCartCount(existCart.getCartCount() + vo.getCartCount());//수량증가
        	
            cartService.insertOrUpdate(existCart); //update
            }
        return "redirect:/cartlist";
    }
    
    //장바구니 수정@@@@@@@@@@@@@@@@@@@@@@@@@@@
    @PatchMapping(value = "/cartlist/{cartNum}")
    public @ResponseBody ResponseEntity<Long> updateCartItem(@PathVariable("cartNum") Long cartNum, int count){
        cartService.updateCartItemCount(cartNum, count);
        return new ResponseEntity<Long>(cartNum, HttpStatus.OK);
    }
    
    //장바구니 수정
    /*@RequestMapping(value="cart/update", method = RequestMethod.GET)
    public String update(Long[] cartNum,  int[] amount, HttpSession session) {
        // session의 id
    	UserVO user = UserVO.builder().userPhone("01011114444").build();
    	session.setAttribute("user", user );
    	//UserVO user = (UserVO)session.getAttribute("user");
        // 레코드의 갯수 만큼 반복문 실행
        for(int i=0; i<cartNum.length; i++){
            CartVO vo = cartService.findById(cartNum[i]);         
            vo.setCartCount(amount[i]);
            cartService.insertOrUpdate(vo);
        }
        return "redirect:/cartlist";
    }*/
    
    //장바구니 삭제
    @RequestMapping(value="/cart/delete", method = RequestMethod.GET)
    public String delete(@RequestParam Long cartNum){
    	System.out.println("cartNum:"+cartNum);
        cartService.delete(cartNum);
        return "redirect:/cartlist";
    }
    
 

   
    
    
}
