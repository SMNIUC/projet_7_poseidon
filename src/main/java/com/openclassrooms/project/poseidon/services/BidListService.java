package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BidListService
{
    private final BidListRepository bidListRepository;

    public List<BidList> getAllBids( )
    {
        List<BidList> allBidsList = new ArrayList<>( );
        bidListRepository.findAll( ).forEach( allBidsList::add );

        return allBidsList;
    }

    public BidList findBidById( Integer bidId )
    {
        return bidListRepository.findById( bidId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid bid Id:" + bidId ) );
    }

    @Transactional
    public void addNewBid( BidList bid )
    {
        bidListRepository.save( bid );
    }

    @Transactional
    public void updateBid( Integer bidId, BidList bid )
    {
        bid.setBidListId( bidId );
        bidListRepository.save( bid );
    }

    @Transactional
    public void deleteBid( Integer bidId )
    {
        BidList bidToDelete = findBidById( bidId );
        bidListRepository.delete( bidToDelete );
    }
}
