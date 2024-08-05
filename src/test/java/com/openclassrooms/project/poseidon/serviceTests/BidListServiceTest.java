package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.repositories.BidListRepository;
import com.openclassrooms.project.poseidon.services.BidListService;
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
public class BidListServiceTest
{
    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListService bidListServiceUnderTest;

    private static BidList bid;

    @BeforeAll
    static void setUp( )
    {
        bid = new BidList( );
        bid.setBidListId( 1 );
        bid.setAccount( "Account Test" );
        bid.setType( "Type Test" );
        bid.setBidQuantity( 10d );
    }

    @Test
    void getAllBids( )
    {
        // GIVEN
        when( bidListRepository.findAll( ) ).thenReturn( List.of( bid ) );

        // WHEN
        List<BidList> bidListTest = bidListServiceUnderTest.getAllBids( );

        // THEN
        assertThat( bidListTest.size( ) ).isEqualTo( 1 );
        assertThat( bidListTest.get( 0 ) ).isEqualTo( bid );
    }

    @Test
    void findBidById( )
    {
        // GIVEN
        when( bidListRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( bid ) );

        // WHEN
        BidList testBid = bidListServiceUnderTest.findBidById( 1 );

        // THEN
        assertThat( testBid ).isEqualTo( bid );
    }

    @Test
    void findBidByIdError( )
    {
        // GIVEN
        when( bidListRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid bid Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> bidListServiceUnderTest.findBidById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewBid( )
    {
        // WHEN
        bidListServiceUnderTest.addNewBid( bid );

        // THEN
        verify( bidListRepository ).save( bid );
    }

    @Test
    void updateBid( )
    {
        // GIVEN
        BidList updatedBid = new BidList( );
        updatedBid.setAccount( "Updated Account" );
        updatedBid.setType( "Updated Type" );
        updatedBid.setBidQuantity( 20d );

        // WHEN
        bidListServiceUnderTest.updateBid( 1, updatedBid );

        // THEN
        verify( bidListRepository ).save( updatedBid );
        assertThat( updatedBid.getBidListId( ) ).isEqualTo( 1 );
    }

    @Test
    void deleteBid( )
    {
        // GIVEN
        when( bidListRepository.findById( 1 ) ).thenReturn( Optional.of( bid ) );

        // WHEN
        bidListServiceUnderTest.deleteBid( 1 );

        // THEN
        verify( bidListRepository ).delete( bid );
    }
}
