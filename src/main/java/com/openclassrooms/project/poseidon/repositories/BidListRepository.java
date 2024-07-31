package com.openclassrooms.project.poseidon.repositories;

import com.openclassrooms.project.poseidon.domain.BidList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidListRepository extends CrudRepository<BidList, Integer>
{
    BidList getBidListByBidListId( Integer bid );
}
