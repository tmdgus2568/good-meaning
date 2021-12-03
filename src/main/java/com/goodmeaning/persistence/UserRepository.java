package com.goodmeaning.persistence;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.UserVO;

public interface UserRepository extends CrudRepository<UserVO, String>{

}
