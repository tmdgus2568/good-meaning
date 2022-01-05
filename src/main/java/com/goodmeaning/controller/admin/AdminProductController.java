package com.goodmeaning.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.goodmeaning.service.admin.AdminProductService;
import com.goodmeaning.util.UpLoadFileUtils;
import com.goodmeaning.vo.PageMaker;
import com.goodmeaning.vo.PageVO;
import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;

@Controller
@RequestMapping("/admin/")
public class AdminProductController {
	
	//브라우저 해석 : static //서버해석 : templates
	
	@Value("${spring.servlet.multipart.location}")
	String locationPath;

	@Autowired
	AdminProductService adminService;
	
	// 상품목록 보여주기
	@GetMapping("/list") //리스트할때부터 온애(VO 2개있음)
	public String getProductListPage(PageVO pageVO, Model model, HttpSession session, HttpServletRequest request,  String colmnName ) { //, ProductOptionVO prdOptVO
		System.out.println(colmnName);
		String sortColumn = colmnName;

		 
		System.out.println("pagevo: " + pageVO);
		int direction = 0;
		if(colmnName =="" || colmnName == null) {
			colmnName = "productCreatedate0";
		}

		direction = Integer.parseInt(colmnName.substring(colmnName.length()-1, colmnName.length()));
		colmnName = colmnName.substring(0, colmnName.length()-1);
		System.out.println(direction + " " + colmnName);
		
		Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

		if (flashMap != null) {
			PageVO pageVO2 = (PageVO) flashMap.get("pageVO");
			if (pageVO2 != null) {
				pageVO = pageVO2;
			}
			int result = (Integer)flashMap.get("deleteresult");
			model.addAttribute("deleteResult", result==0?"삭제할 수 없습니다.":"삭제되었습니다.");

		}

		if (pageVO == null) //맨 처음 null일때 
			pageVO = PageVO.builder().page(1).size(12).type(null).keyword(null).build();

		Page<ProductVO> result = adminService.productList(pageVO, direction, colmnName); //확인해보기
		
		model.addAttribute("productPaging", new PageMaker<>(result));
		model.addAttribute("pageVO", pageVO);
		model.addAttribute("sortColumn", sortColumn);
		model.addAttribute("totalCount", adminService.selectProductAll(pageVO));
         
		return "admin/product/list";
	}
	
	// 상품등록 페이지
	@GetMapping("/productregister")
	public String registerProduct(HttpSession session) {
		//sessions 넣기
		return "admin/product/register"; //forward 
	}
	
	// 상품등록
	@PostMapping("/productregister") //들어올때의 값들 
	public String insertProduct(ProductVO product, String[] optionName, int[] optionPrice, String optionCategory, String optionCategory2) { //, HttpServletRequest request
		
		System.out.println("optionCategory2" + optionCategory2);
		if(optionCategory.equals("Other")) optionCategory = optionCategory2;
		
		//html에서 입력정보 들어오는지 확인
		System.out.println(product);
		System.out.println(Arrays.toString(optionName) + Arrays.toString(optionPrice));
				
		MultipartFile[] uploadfiles = product.getUploadFile(); //5개 파일정보 넘어옴
		List<String> fileNames = new ArrayList<>();  
		
		// 이미지를 업로드할 폴더를 설정 = /uploadPath/imgUpload (저장하려고 찾아가는 것)
		String uploadPath = locationPath + File.separator + "upload";
		// 위 폴더 기준 연월 폴더 생성
		String ymdPath = UpLoadFileUtils.calcPath(uploadPath);
		
		
		for(MultipartFile uploadfile:uploadfiles){
			String fileName = null;
			if(uploadfile.getOriginalFilename() != null && !uploadfile.getOriginalFilename().equals("")) {
				try {
					fileName = UpLoadFileUtils.fileUpload(uploadPath, uploadfile.getOriginalFilename(), uploadfile.getBytes(), ymdPath);
					fileName = "upload" + ymdPath + File.separator + fileName;
					System.out.println("fileName=" + fileName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {  // 첨부된 파일이 없으면
				continue;
			}
			fileNames.add(fileName);
		}
		
		int size = fileNames.size();
		product.setProductDetailimg(fileNames.get(size-1));
		product.setProductMainimg1(fileNames.get(0));
		for(int idx = 1; idx < size-1; idx++) {
			switch(idx) {
			case 1: 
				product.setProductMainimg2(fileNames.get(idx));
				break;				
			case 2: 
				product.setProductMainimg3(fileNames.get(idx));
				break;				
			case 3: 
				product.setProductMainimg4(fileNames.get(idx));
				break;				
			}
		}
		adminService.insertProduct(product, optionName, optionPrice, optionCategory);
		
		return "redirect:list"; // /list 절대경로 요청주소(mapping으로)
	}
	
	//상품딕테일 보여주기
	@GetMapping("/detail")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getDetail(Long productNum) {//html에서 받을때 @RequestBody(url, data)
		Map<String, Object> map = new HashMap<>();
		System.out.println(productNum);
		ProductVO product = adminService.selectById(productNum);
		List<ProductOptionVO> productOption = adminService.findByProductNum(product); //레파지토리 호출
		map.put("product", product);
		map.put("productOption", productOption);
		
		System.out.println("pvo : " + map);
		
		//ProductOptionVO 레파지토리에서 findbyproductnum생성
		 //컬럼값으로 옴.productNum is present > optional > get으로 받아옴
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	} 
	
	//상품수정
	@PostMapping("/updateProduct")
	public String updateProduct(ProductVO product, String[] file, String[] optionName, Integer[] optionPrice,
			String optionCategory, String writeOther, Long[] optionNum, int options, PageVO pageVO,
			RedirectAttributes reAttr) throws IOException, Exception {
		System.out.println(Arrays.toString(product.getUploadFile()) );
		System.out.println("update pvo : " + product);
		System.out.println("update optionName : " + Arrays.toString(optionName));
		System.out.println("update optionName length : " + optionName.length);
		System.out.println("update optionPrice : " + Arrays.toString(optionPrice));
		System.out.println("update optionNum : " + Arrays.toString(optionNum));
		System.out.println("update optionNum length : " + optionNum.length);
		System.out.println("update optionCategory : " + optionCategory);
		System.out.println("update optionCategory2 : " + writeOther);
		
		//product = adminService.findById(product.getProductNum());
		//product.set
		if (optionCategory.equals("Other"))
			optionCategory = writeOther;
		
		System.out.println("options: " + options);

		//locationPath
		
		MultipartFile[] multiFile = product.getUploadFile();
		System.out.println(Arrays.toString(file));
 
		for (int i = 0; multiFile != null && i < multiFile.length; i++) {

			String imgUploadPath = locationPath + File.separator + "upload";
			String ymdPath = UpLoadFileUtils.calcPath(imgUploadPath); // 위의 폴더를 기준으로 연월일 폴더를 생성
			String fileName = null;
			String originalName = multiFile[i].getOriginalFilename();
			System.out.println("'" + originalName + "'");

			if (originalName != null && !originalName.equals("")) {
				// upload하기
				fileName = UpLoadFileUtils.fileUpload(imgUploadPath, originalName, multiFile[i].getBytes(), ymdPath);
				fileName = "upload" + ymdPath + File.separator + fileName;
				columnSetting(i, fileName, product);
				//현재 게시판에 존재하는 파일객체를 만듬 
				//File file = new File(path + "\\" + "저장된 파일 이름"); 
				//if(file.exists()) { // 파일이 존재하면 
				//	file.delete(); // 파일 삭제 }
				//}

				
			} else {
				if (!(file[i].equals("")))
					columnSetting(i, file[i], product);
				
			} 
		}
		System.out.println("수정후 프로덕트 + " + product);
		adminService.updateProduct(product, optionName, optionPrice, optionCategory, optionNum, options);
		reAttr.addFlashAttribute("pageVO", pageVO);
		return "redirect:list";
		
	}
	
	private void columnSetting(int i, String fileName, ProductVO product) {
		switch (i) {
		case 0:
			product.setProductMainimg1(fileName);
			break;
		case 1:
			product.setProductMainimg2(fileName);
			break;
		case 2:
			product.setProductMainimg3(fileName);
			break;
		case 3:
			product.setProductMainimg4(fileName);
			break;
		case 4:
			product.setProductDetailimg(fileName);
			break;

		}

	}
	
	// 상품 삭제
	@GetMapping("/deleteProduct")
	public String deleteProduct(Long productNum, RedirectAttributes reAttr, Model model, PageVO pageVO) {
		System.out.println("delete:" + pageVO);
		System.out.println("productNum:" + productNum);
		int result = adminService.deleteByProductNum(productNum);
		model.addAttribute("pageVO", pageVO);
		reAttr.addFlashAttribute("pageVO", pageVO);
		reAttr.addFlashAttribute("deleteResult", result);
		return "redirect:list";
	}
	
	
	// CRUD
	



//	@RequestMapping(value = "/update", method = RequestMethod.POST)
//	public String update(WebBoard board, RedirectAttributes re_attr, PageVO pageVO) {
//		System.out.println(board);
//		System.out.println("detail post:" + pageVO);
//		WebBoard updateBoard = webRepo.save(board);
//		re_attr.addFlashAttribute("resultmsg", updateBoard == null ? "update실패" : "update성공");
//		re_attr.addFlashAttribute("pageVO", pageVO);
//		return "redirect:/list";
//	}
//
//	@RequestMapping(value = "/detail", method = RequestMethod.GET)
//	public String selectById(Long bno, Model model, PageVO pageVO) {
//		// 상세보기할때 PageVO(현재패이지, 페이지사이즈, 조건type, 조건keyword)그리고 Board의 bno가 필요하다.
//		System.out.println("detail get:" + pageVO);
//		WebBoard board = webRepo.findById(bno).orElse(null);
//		model.addAttribute("board", board);
//		model.addAttribute("pageVO", pageVO);
//		return "webBoard/detail";
//	}

}
