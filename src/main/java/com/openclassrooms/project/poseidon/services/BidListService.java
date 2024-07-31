package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.repositories.BidListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
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
        return bidListRepository.getBidListByBidListId( bidId );
    }

    @Transactional
    public void addNewBid( BidList bid )
    {
        bidListRepository.save( bid );
    }

    @Transactional
    public void updateBid( Integer bidId, BidList bid )
    {
        BidList bidToUpdate = findBidById( bidId );
        bidToUpdate.setAccount( bid.getAccount( ) );
        bidToUpdate.setType( bid.getType( ) );
        bidToUpdate.setBidQuantity( bid.getBidQuantity( ) );
        bidListRepository.save( bidToUpdate );
    }

    @Transactional
    public void deleteBid( Integer bidId )
    {
        BidList bidToDelete = findBidById( bidId );
        bidListRepository.delete( bidToDelete );
    }
}
