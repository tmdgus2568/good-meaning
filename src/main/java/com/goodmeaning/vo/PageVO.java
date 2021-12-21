package com.goodmeaning.vo;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class PageVO {

	private static final int DEFAULT_SIZE = 12;
	private static final int DEFAULT_MAX_SIZE = 50;
	
	int page; //몇번째
	int size; //한페이지 당 몇개
	String[] type; //조건
	Object[] keyword; //조건에 해당하는 키워드
	
	public PageVO() {
		this.page = 1;
		this.size = DEFAULT_SIZE;
	}

	public void setPage(int page) {
		this.page = page < 0 ? 1 : page;
	}

	public void setSize(int size) {
		this.size = size < DEFAULT_SIZE || size > DEFAULT_MAX_SIZE ? DEFAULT_SIZE : size;
	}

	public Pageable makePaging(int direction, String...props) {
		Sort.Direction dir = direction==0?Direction.DESC:Direction.ASC; //0 desc, 1 asc
		return PageRequest.of(this.page-1, this.size , dir, props );
		////0부터 시작 size수만큼 prop기준으로 dir방향정렬

	}
	public void setType(String[] type) {
		this.type = type;
	}

	public void setKeyword(Object[] keyword) {
		this.keyword = keyword;
	}
}
