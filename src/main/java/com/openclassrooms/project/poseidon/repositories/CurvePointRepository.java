package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.CurvePoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurvePointRepository extends CrudRepository<CurvePoint, Integer>
{
}
