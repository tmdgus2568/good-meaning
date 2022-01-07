package com.goodmeaning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.vo.ProductVO;

@Service
public class MainService {

	@Autowired
	ProductRepository productRepo;
	
	public List<ProductVO> findNewlist(){

		Sort sort = Sort.by(Sort.Direction.DESC, "productCreatedate");
		List<ProductVO> newlist = productRepo.findAll(sort);
		if(newlist.size()>8) {
			newlist = newlist.subList(0, 8);
		}
		System.out.println(newlist.size());
		return newlist;
		
	}
	
	public List<ProductVO> findBestlist(){

		Sort sort = Sort.by(Sort.Direction.ASC, "productCreatedate");
		List<ProductVO> newlist = productRepo.findAll(sort);
		if(newlist.size()>8) {
			newlist = newlist.subList(0, 8);
		}
		System.out.println(newlist.size());
		return newlist;
		
	}
}
