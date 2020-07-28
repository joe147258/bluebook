package com.bluebook.repository;


import com.bluebook.domain.Classroom;

import org.springframework.data.repository.CrudRepository;

public interface ClassroomRepository extends CrudRepository<Classroom, Integer> {

	Classroom findById(int id);
	Classroom findByJoinCode(String joinCode);



}