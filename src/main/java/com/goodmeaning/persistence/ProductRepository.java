package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ProductVO;

public interface ProductRepository  extends CrudRepository<ProductVO, Long> {

}