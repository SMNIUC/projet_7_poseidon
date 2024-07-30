package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.Trade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Integer>
{
}
