package uol.bluebook.repositories;

import org.springframework.data.repository.CrudRepository;

import uol.bluebook.domain.CustomUser;

public interface UserRepository extends CrudRepository<CustomUser, Integer> {

	CustomUser findByUsername(String username);
	CustomUser findById(int id);
    
}