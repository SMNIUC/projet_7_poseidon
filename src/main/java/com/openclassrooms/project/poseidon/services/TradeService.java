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

    public List<Trade> getAllTrades( )
    {
        List<Trade> allTradesList = new ArrayList<>( );
        tradeRepository.findAll( ).forEach( allTradesList::add );
        return allTradesList;
    }

    public Trade findTradeById( Integer tradeId )
    {
        return tradeRepository.getTradeByTradeId( tradeId );
    }

    @Transactional
    public void addNewTrade( Trade trade )
    {
        tradeRepository.save( trade );
    }

    @Transactional
    public void updateTrade( Integer tradeId, Trade trade )
    {
        Trade tradeToUpdate = findTradeById( tradeId );
        tradeToUpdate.setAccount( trade.getAccount( ) );
        tradeToUpdate.setType( trade.getType( ) );
        tradeToUpdate.setBuyQuantity( trade.getBuyQuantity( ) );
        tradeRepository.save( tradeToUpdate );
    }

    @Transactional
    public void deleteTrade( Integer tradeId )
    {
        Trade tradeToDelete = findTradeById( tradeId );
        tradeRepository.delete( tradeToDelete );
    }
}
