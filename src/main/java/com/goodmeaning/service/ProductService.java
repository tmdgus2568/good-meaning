package com.goodmeaning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.ProductRepository;
import com.goodmeaning.vo.ProductVO;

@Service
public class ProductService {

	@Autowired
	ProductRepository prepo;
	
	public ProductVO selectById(Long pid) {//CartController참조
		return prepo.findById(pid).orElse(null);
	}
}
