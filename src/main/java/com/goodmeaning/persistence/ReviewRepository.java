package com.goodmeaning.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ProductVO;
import com.goodmeaning.vo.ReviewVO;

public interface ReviewRepository extends CrudRepository<ReviewVO, Long>{

	//List<ReviewVO> findByProduct(ProductVO product);
	
}
