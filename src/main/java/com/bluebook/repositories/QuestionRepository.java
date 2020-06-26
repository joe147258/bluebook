package com.bluebook.repositories;


import com.bluebook.domain.TestQuestion;

import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<TestQuestion, Integer> {

	TestQuestion findById(int id);
    
}