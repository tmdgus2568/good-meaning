package com.goodmeaning.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;

public interface ProductOptionRepository extends CrudRepository<ProductOptionVO, Long> {

	
	public ProductOptionVO findByProductNum(ProductVO product);
}
