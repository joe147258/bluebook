package com.bluebook.repositories;

import com.bluebook.domain.CustomUser;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<CustomUser, Integer> {

	CustomUser findByUsername(String username);
	CustomUser findById(int id);
    
}