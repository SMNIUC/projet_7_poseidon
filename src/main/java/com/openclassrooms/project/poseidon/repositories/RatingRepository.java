package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Integer>
{
}
