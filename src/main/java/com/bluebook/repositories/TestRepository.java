package com.bluebook.repositories;

import javax.transaction.Transactional;

import com.bluebook.domain.Test;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface TestRepository extends CrudRepository<Test, Integer> {

	Test findById(int id);
	
	@Modifying
	@Transactional
	@Query("update Test t set t.published = ?1 where t.id = ?2")
	void updatePublished(Boolean published, Integer testId);


}