package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.Trade;
import com.openclassrooms.project.poseidon.repositories.TradeRepository;
import com.openclassrooms.project.poseidon.services.TradeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest
{
    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeService tradeServiceUnderTest;

    private static Trade trade;

    @BeforeAll
    static void setUp( )
    {
        trade = new Trade( );
        trade.setTradeId( 1 );
        trade.setAccount( "Account test" );
        trade.setType( "Type test" );
        trade.setBuyQuantity( 10d );
    }

    @Test
    void getAllTrades( )
    {
        // GIVEN
        when( tradeRepository.findAll( ) ).thenReturn( List.of( trade ) );

        // WHEN
        List<Trade> tradeList = tradeServiceUnderTest.getAllTrades( );

        // THEN
        assertThat( tradeList.size( ) ).isEqualTo( 1 );
        assertThat( tradeList.get( 0 ) ).isEqualTo( trade );
    }

    @Test
    void findTradeById( )
    {
        // GIVEN
        when( tradeRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( trade ) );

        // WHEN
        Trade tradeTest = tradeServiceUnderTest.findTradeById( 1 );

        // THEN
        assertThat( tradeTest ).isEqualTo( trade );
    }

    @Test
    void findTradeByIdError( )
    {
        // GIVEN
        when( tradeRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid trade Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> tradeServiceUnderTest.findTradeById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewTrade( )
    {
        // WHEN
        tradeServiceUnderTest.addNewTrade( trade );

        // THEN
        verify( tradeRepository ).save( trade );
    }

    @Test
    void updateTrade( )
    {
        // GIVEN
        Trade updatedTrade = new Trade( );
        updatedTrade.setBuyQuantity( 20d );

        // WHEN
        tradeServiceUnderTest.updateTrade( 1, updatedTrade );

        // THEN
        verify( tradeRepository ).save( updatedTrade );
        assertThat( updatedTrade.getTradeId( ) ).isEqualTo( 1 );
    }

    @Test
    void deleteTrade( )
    {
        // GIVEN
        when( tradeRepository.findById( 1 ) ).thenReturn( Optional.of( trade ) );

        // WHEN
        tradeServiceUnderTest.deleteTrade( 1 );

        // THEN
        verify( tradeRepository ).delete( trade );
    }
}
