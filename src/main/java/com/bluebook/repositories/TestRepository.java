package com.bluebook.repositories;


import com.bluebook.domain.Test;

import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<Test, Integer> {

	Test findById(int id);
    
}