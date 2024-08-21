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

    /**
     * Gets all the available bids from the database
     *
     * @return a list of bids
     */
    public List<BidList> getAllBids( )
    {
        List<BidList> allBidsList = new ArrayList<>( );
        bidListRepository.findAll( ).forEach( allBidsList::add );

        return allBidsList;
    }

    /**
     * Finds a bid in the database from its id
     *
     * @param bidId the id of the bid to be found
     * @return a bid
     */
    public BidList findBidById( Integer bidId )
    {
        return bidListRepository.findById( bidId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid bid Id:" + bidId ) );
    }

    /**
     * Creates and saves a new bid in the database
     *
     * @param bid the bid to be saved
     */
    @Transactional
    public void addNewBid( BidList bid )
    {
        bidListRepository.save( bid );
    }

    /**
     * Updates a bid in the database with new info
     *
     * @param bidId the id of the bid to be updated
     * @param bid a bid object that holds the new bid information
     */
    @Transactional
    public void updateBid( Integer bidId, BidList bid )
    {
        bid.setBidListId( bidId );
        bidListRepository.save( bid );
    }

    /**
     * Deletes a bid in the database, based on its id
     *
     * @param bidId the id of the bid to be deleted
     */
    @Transactional
    public void deleteBid( Integer bidId )
    {
        BidList bidToDelete = findBidById( bidId );
        bidListRepository.delete( bidToDelete );
    }
}
