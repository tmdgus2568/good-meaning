package com.goodmeaning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodmeaning.persistence.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	ReviewRepository rrepo;
	
	
}
