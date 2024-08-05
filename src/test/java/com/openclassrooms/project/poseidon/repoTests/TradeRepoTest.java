package com.openclassrooms.project.poseidon.repoTests;

import com.openclassrooms.project.poseidon.domain.Trade;
import com.openclassrooms.project.poseidon.repositories.TradeRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeRepoTest
{
    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void tradeTest( )
    {
        Trade trade = new Trade( );
        trade.setAccount( "Trade Account" );
        trade.setType( "Type" );
        trade.setBuyQuantity( 10d );

        // Save
        trade = tradeRepository.save(trade);
        Assert.assertNotNull(trade.getTradeId());
        Assert.assertEquals( "Trade Account", trade.getAccount( ) );

        // Update
        trade.setAccount("Trade Account Update");
        trade = tradeRepository.save(trade);
        Assert.assertEquals( "Trade Account Update", trade.getAccount( ) );

        // Find
        List<Trade> listResult = ( List<Trade> ) tradeRepository.findAll();
        Assert.assertFalse( listResult.isEmpty( ) );

        // Delete
        Integer id = trade.getTradeId();
        tradeRepository.delete(trade);
        Optional<Trade> tradeList = tradeRepository.findById(id);
        Assert.assertFalse(tradeList.isPresent());
    }
}
