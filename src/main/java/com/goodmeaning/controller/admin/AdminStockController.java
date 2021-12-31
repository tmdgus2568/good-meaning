package com.goodmeaning.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.service.admin.AdminStockService;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;

@Controller
@RequestMapping("/admin/")
public class AdminStockController {
	
	@Autowired
	AdminStockService stockService;

	@GetMapping("/stock") //리스트할때부터 온애(VO 2개있음)
	public String getStockListPage(PageVO pageVO, Model model, HttpSession session, HttpServletRequest request, String colmnName) { //, ProductOptionVO prdOptVO
		//sessions 넣기
		System.out.println("pagevo: " + pageVO);
		int direction = 0;

		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

		if (flashMap != null) {
			PageVO pageVO2 = (PageVO) flashMap.get("pageVO");
			if (pageVO2 != null) {
				pageVO = pageVO2;
			}
		}

		if (pageVO == null) //맨 처음 null일때 
			pageVO = PageVO.builder().page(1).size(12).type(null).keyword(null).build();
		if (colmnName == null || colmnName.equals(""))
			colmnName = "product_num";
		
		Page<Object[]> result = stockService.stockList(pageVO, direction, colmnName); //확인해보기
		System.out.println(new PageMaker<>(result));
		model.addAttribute("stockPaging", new PageMaker<>(result));
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("totalCount", stockService.selectAll(pageVO));
         
		return "admin/stock/stock";
	}
}
