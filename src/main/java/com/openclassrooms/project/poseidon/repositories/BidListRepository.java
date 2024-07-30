package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.BidList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidListRepository extends CrudRepository<BidList, Integer>
{
//    List<BidList> findByBidderId( Integer bid );
}
