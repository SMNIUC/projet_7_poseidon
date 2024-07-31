package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.RuleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleNameRepository extends CrudRepository<RuleName, Integer>
{
}
