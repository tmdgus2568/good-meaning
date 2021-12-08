package com.goodmeaning.persistence;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.goodmeaning.vo.UserVO;

public interface UserRepository extends CrudRepository<UserVO, String>{
	Optional<UserVO> findByUserIdAndUserPw(String userId, String userPw);

}
