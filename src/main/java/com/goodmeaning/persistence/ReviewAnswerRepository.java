package com.goodmeaning.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.ReviewAnswerVO;
import com.goodmeaning.vo.ReviewVO;

public interface ReviewAnswerRepository  extends CrudRepository<ReviewAnswerVO, Long> {

	List<ReviewAnswerVO> findByReviewNum(ReviewVO review);
	
}
