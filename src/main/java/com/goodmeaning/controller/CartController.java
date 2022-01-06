package com.goodmeaning.controller;

import java.util.List;

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
import com.goodmeaning.vo.ProductOptionVO;
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

	@RequestMapping("/cartlist") // 브라우저에서 요청하는 코드
	public String selectAll(HttpSession session, Model model) {
		UserVO user = (UserVO) session.getAttribute("user");

		model.addAttribute("clist", cartService.findAllCartList(user.getUserPhone()));
		return "user/cart/cartlist";
	}

	// 장바구니 추가Test
	@GetMapping("/addcart")
	public String insertget() {
		return "user/cart/addcart";
	}

	@PostMapping("/cart")
	public String insert(CartVO vo, Long optionNum, Long productNum, HttpSession session) {
		// 로그인된 유저 확인
		System.out.println("cartvo:" + vo);
		UserVO user = (UserVO) session.getAttribute("user");
		vo.setUserPhone(user);
		ProductVO product = productService.selectById(productNum);
		vo.setProductNum(product);
		System.out.println("optionNum=" + optionNum);
		if (optionNum != null && optionNum != 0)
			vo.setProductOption(porepo.findById(optionNum).get());
		// 장바구니에 기존 상품이 있는지 검사
		int count = cartService.countCart(productNum, user.getUserPhone());

		if (count == 0) {
			// 없으면 insert
			cartService.insertOrUpdate(vo);
		} else {
			// 있으면 update
			CartVO existCart = cartService.findByProductNumAndUserPhone(product, user);
			existCart.setCartCount(existCart.getCartCount() + vo.getCartCount());// 수량증가

			cartService.insertOrUpdate(existCart); // update
		}
		return "redirect:/cartlist";
	}

	// 장바구니 수정
	@PatchMapping(value = "/cartlist/{cartNum}")
	public @ResponseBody ResponseEntity<Long> updateCartItem(@PathVariable("cartNum") Long cartNum, int count) {
		cartService.updateCartItemCount(cartNum, count);
		return new ResponseEntity<Long>(cartNum, HttpStatus.OK);
	}


	// 장바구니 삭제
	@RequestMapping(value = "/cart/delete", method = RequestMethod.GET)
	public String delete(@RequestParam Long cartNum) {
		System.out.println("cartNum:" + cartNum);
		cartService.delete(cartNum);
		return "redirect:/cartlist";
	}

	@RequestMapping(value = "/cartclick", method = RequestMethod.POST)
	@ResponseBody
	public void cart(@RequestParam(value = "optionnum[]", required = false) List<Long> optionnum,
			@RequestParam(value = "opquantity[]", required = false) List<Integer> opquantity, Long pronum,
			Integer proquantity, HttpSession session) {

		if (opquantity == null) {
			CartVO cart = new CartVO();

			UserVO user = (UserVO) session.getAttribute("user"); // 유저
			cart.setUserPhone(user);
			ProductVO product = productService.selectById(pronum);
			cart.setProductNum(product);

			int price = product.getProductPrice();
			cart.setCartPrice(price);

			// 장바구니에 기존 상품이 있는지 검사
			int count = cartService.countCart(pronum, user.getUserPhone());

			if (count == 0) {
				// 없으면 insert
				cart.setCartCount(proquantity);
				cartService.insertOrUpdate(cart);
			} else {
				// 있으면 update
				CartVO existCart = cartService.findByProductNumAndUserPhone(product, user);
				existCart.setCartCount(existCart.getCartCount() + proquantity);// 수량증가

				cartService.insertOrUpdate(existCart); // update
			}

		}

		for (int i = 0; opquantity != null && i < opquantity.size(); i++) {
			CartVO cart = new CartVO();

			System.out.println(opquantity.get(i));
			System.out.println(optionnum.get(i));

			UserVO user = (UserVO) session.getAttribute("user"); // 유저
			cart.setUserPhone(user);
			ProductVO product = productService.selectById(pronum);
			cart.setProductNum(product);

			ProductOptionVO option = porepo.findById(optionnum.get(i)).get();
			
			int price = product.getProductPrice() + option.getExtraprice();
			cart.setCartPrice(price);

			if (optionnum.get(i) != null && optionnum.get(i) != 0)
				cart.setProductOption(porepo.findById(optionnum.get(i)).get());

			// 장바구니에 기존 상품이 있는지 검사
			//int count = cartService.countCart(pronum, user.getUserPhone());
			CartVO existCart = cartService.findByProductNumAndUserPhoneAndProductOption(product, user, option);
			
			if (existCart == null) {
				// 없으면 insert
				cart.setCartCount(opquantity.get(i));
				cartService.insertOrUpdate(cart);
			} else {
				// 있으면 update
				existCart.setCartCount(existCart.getCartCount() + opquantity.get(i));// 수량증가

				cartService.insertOrUpdate(existCart); // update
			}
			System.out.println(cart);
		}

	}

}
