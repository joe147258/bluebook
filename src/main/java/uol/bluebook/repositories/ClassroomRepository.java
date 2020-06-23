package uol.bluebook.repositories;


import org.springframework.data.repository.CrudRepository;


import uol.bluebook.domain.Classroom;

public interface ClassroomRepository extends CrudRepository<Classroom, Integer> {

	Classroom findById(int id);
	Classroom findByJoinCode(String joinCode);



}