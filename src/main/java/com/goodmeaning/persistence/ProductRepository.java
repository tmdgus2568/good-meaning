package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import com.goodmeaning.vo.ProductVO;

public interface ProductRepository  extends CrudRepository<ProductVO, Long> {

	Optional<ProductVO> findByProductName(String pname);
}