package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.Trade;
import com.openclassrooms.project.poseidon.repositories.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TradeService
{
    final private TradeRepository tradeRepository;

    /**
     * Gets all the available trades from the database
     *
     * @return a list of trades
     */
    public List<Trade> getAllTrades( )
    {
        List<Trade> allTradesList = new ArrayList<>( );
        tradeRepository.findAll( ).forEach( allTradesList::add );

        return allTradesList;
    }

    /**
     * Finds a trade in the database from its id
     *
     * @param tradeId the id of the trade to be found
     * @return a trade
     */
    public Trade findTradeById( Integer tradeId )
    {
        return tradeRepository.findById( tradeId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid trade Id:" + tradeId ) );
    }

    /**
     * Creates and saves a new trade in the database
     *
     * @param trade the trade to be saved
     */
    @Transactional
    public void addNewTrade( Trade trade )
    {
        tradeRepository.save( trade );
    }

    /**
     * Updates a trade in the database with new info
     *
     * @param tradeId the id of the trade to be updated
     * @param trade a trade object that holds the new trade information
     */
    @Transactional
    public void updateTrade( Integer tradeId, Trade trade )
    {
        trade.setTradeId( tradeId );
        tradeRepository.save( trade );
    }

    /**
     * Deletes a trade in the database, based on its id
     *
     * @param tradeId the id of the trade to be deleted
     */
    @Transactional
    public void deleteTrade( Integer tradeId )
    {
        Trade tradeToDelete = findTradeById( tradeId );
        tradeRepository.delete( tradeToDelete );
    }
}
