package com.goodmeaning.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

@Getter
@ToString(exclude = "pageList")
@Log
public class PageMaker<T> {

	private Page<T> result; //현재 페이지에 있는 누구
	private Pageable prevPage; //이전으로 이동하는데 필요한 정보를 가짐
	private Pageable nextPage; //다음 페이지 정보
	private Pageable currentPage; //현재 페이지 정보
	private int currentPageNum;  //화면에 보이는 1부터 시작하는 페이지번호
	private int totalPageNum; //총 전체 페이지
	private List<Pageable> pageList; //페이징이 된 모든 정보 담겨있음(한화면에서 나오는 페이지 갯수)
	
	public PageMaker(Page<T> result) {
		
		this.result = result;
		this.currentPage = result.getPageable();
		this.currentPageNum = currentPage.getPageNumber()+1; //페이지 0부터 시작
		this.totalPageNum = result.getTotalPages();
		this.pageList = new ArrayList<Pageable>();
		calcPage();
	}
	
	public void calcPage() {//pagelist의 갯수를 default로 5개로 잡음(한 화면에 5개 출력)
		int cnt = 5;
		int tempEndNum = (int)(Math.ceil(currentPageNum/(cnt*1.0))*cnt); //5개중 마지막숫자 
		//1,2,3,4,5 더보기 
		//6,,,,,,10
		int startNum = tempEndNum - (cnt-1) ;
		Pageable startPage = this.currentPage;
		for(int i = startNum; i<this.currentPageNum; i++) {
			startPage = startPage.previousOrFirst();  //20페이지라면 1페이지….40페이지라면 21페이지 
		}
		this.prevPage = startPage.getPageNumber()<=0?null:startPage.previousOrFirst();
		log.info("tempEndNum:" + tempEndNum);
		log.info("totalPageNum:" + totalPageNum);
		if(this.totalPageNum<tempEndNum) {
			tempEndNum = this.totalPageNum;
			this.nextPage = null;
		}
		
		for(int i = startNum; i<=tempEndNum; i++) {
			pageList.add(startPage);
			startPage = startPage.next();
		}
		this.nextPage = startPage.getPageNumber()+1 < totalPageNum?startPage:null;
	}
}
