package com.goodmeaning.controller.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.service.admin.AdminStockService;
import com.goodmeaning.vo.OrderDetailVO;
import com.goodmeaning.vo.OrderVO;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.PurchaseVO;

@Controller
@RequestMapping("/admin/")
public class AdminStockController {
	
	@Autowired
	AdminStockService stockService;
	

	//재고리스트 totalStock
	@GetMapping("/stock") //리스트할때부터 온애(VO 2개있음)
	public String getStockListPage(PageVO pageVO, Model model, HttpServletRequest request, String colmnName) {
		
		String sortColumn = colmnName;
		System.out.println("pagevo: " + pageVO);
		int direction = 0;
		if (colmnName == null || colmnName.equals(""))
			colmnName = "product_num0";
		
		direction = Integer.parseInt(colmnName.substring(colmnName.length()-1, colmnName.length()));
		colmnName = colmnName.substring(0, colmnName.length()-1);
		System.out.println(direction + " " + colmnName);
		
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

		if (flashMap != null) {
			PageVO pageVO2 = (PageVO) flashMap.get("pageVO");
			if (pageVO2 != null) {
				pageVO = pageVO2;
			}
			//성공메세지보내기
			Object result = flashMap.get("result");
			if(result != null) {
				String resultMsg =  (String)result;
				model.addAttribute("result", resultMsg);
			}
		}

		if (pageVO == null) //맨 처음 null일때 
			pageVO = PageVO.builder().page(1).size(12).type(null).keyword(null).build();
		
		Page<Object[]> result = stockService.stockList(pageVO, direction, colmnName); //확인해보기
		int resultCount = (int) result.getTotalElements();
		
		model.addAttribute("stockPaging", new PageMaker<>(result));
		model.addAttribute("sortColumn", sortColumn);
		model.addAttribute("totalCount", resultCount);
		model.addAttribute("pageVO", pageVO);
         
		return "admin/stock/stock";
	}
	
	//재고딕테일
	@GetMapping("/stockinfo")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDetail(Long productNum, Long optionNum) {//html에서 받을때 @RequestBody(url, data)
		Map<String, Object> map = new HashMap<>();
		System.out.println("ajax pno: " + productNum);
		System.out.println("ajax ono: " + optionNum);
		ProductVO product = stockService.findByProductNum(productNum);
		
		ProductOptionVO productOption = stockService.findByOptionNum(optionNum); //레파지토리 호출
		System.out.println("ajax ono after: " + optionNum);
		map.put("product", product);
		map.put("productOption", productOption);
		
		System.out.println("pvo : " + map);
		
		//ProductOptionVO 레파지토리에서 findbyproductnum생성
		 //컬럼값으로 옴.productNum is present > optional > get으로 받아옴
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	} 
	
	
	//입출고내역가져오기
	@GetMapping("/history")
	public String getHistoryPage(PageVO pageVO, Model model, HttpServletRequest request) {
		
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

		if (flashMap != null) {
			PageVO pageVO2 = (PageVO) flashMap.get("pageVO");
			if (pageVO2 != null) {
				pageVO = pageVO2;
			}
			Object result = flashMap.get("result");
			if(result != null) {
				String resultMsg =  (String)result;
				model.addAttribute("result", resultMsg);
			} //return할때부터 메세지값을 주고 오기
		}
		
//		}
		int direction = 0;
		String sortColumn1 = "purchaseDate";
		String sortColumn2 = "orderDate"; //orderDetailNum
		if (pageVO == null) //맨 처음 null일때 
			pageVO = PageVO.builder().page(1).size(12).type(null).keyword(null).build();
		
		Page<PurchaseVO> result1 = stockService.historyList(pageVO, direction, sortColumn1);
		model.addAttribute("purchaseHistory", new PageMaker<>(result1));
		model.addAttribute("totalCountP", stockService.selectHistoryAll());
		
		Page<OrderVO> result2 = stockService.ordersList(pageVO, direction, sortColumn2);
		model.addAttribute("orderHistory", new PageMaker<>(result2));
		model.addAttribute("totalCountO", stockService.selectOrderAll());
		model.addAttribute("pageVO", pageVO);
         
		return "admin/stock/history";
	}
	
	// 입고등록
	@PostMapping("/stockregister") //들어올때의 값들 
	public String insertStock(Long productNum, Long optionNum, int purchaseQuantity, RedirectAttributes reAttr, PageVO pageVO) {
		
		System.out.println("입고등록 때 오는 pageVO : " + pageVO);
		String insertResult = stockService.insertUpdate(productNum, optionNum, purchaseQuantity);
		reAttr.addFlashAttribute("result",  insertResult);
		reAttr.addFlashAttribute("pageVO", pageVO);
		return "redirect:stock"; // /list 절대경로 요청주소(mapping으로)
	}
	
	// 입고삭제
	@PostMapping("/cancel")
	public String cancelPurchase(Long purchaseNum, Long optionNum, Long productNum, int purchaseQuantity, RedirectAttributes reAttr) {
		String cancelPurchase = stockService.cancelPurchase(purchaseNum, optionNum, productNum , purchaseQuantity);
		reAttr.addFlashAttribute("result",  cancelPurchase);
		return "redirect:history";
	}
	
}
