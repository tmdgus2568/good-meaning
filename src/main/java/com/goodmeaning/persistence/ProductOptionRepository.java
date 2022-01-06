package com.goodmeaning.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ProductOptionVO;
import com.goodmeaning.vo.ProductVO;

public interface ProductOptionRepository extends CrudRepository<ProductOptionVO, Long> {
	
	public List<ProductOptionVO> findByProductNum(ProductVO product);
	
	@Modifying
	@Query(value = "delete from tbl_product_option where product_num = ?1", nativeQuery = true)
	public void deleteByProductNum(Long productNum);

}
