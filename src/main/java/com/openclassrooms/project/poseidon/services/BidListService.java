package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BidListService
{
    private final BidListRepository bidListRepository;

//    public List<BidList> getAllBidLists( Integer bid )
//    {
//        List<BidList> allBidList;
//        return allBidList = bidListRepository.findByBidderId( bid );
//    }
}
