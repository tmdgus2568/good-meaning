package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ProductOptionVO;

public interface ProductOptionRepository extends CrudRepository<ProductOptionVO, Long> {

	public ProductOptionVO findByProductNum(Long productNum);
}
