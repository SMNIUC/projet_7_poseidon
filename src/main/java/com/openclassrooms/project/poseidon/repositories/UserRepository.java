package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
    User findByUsername( String email );
}
