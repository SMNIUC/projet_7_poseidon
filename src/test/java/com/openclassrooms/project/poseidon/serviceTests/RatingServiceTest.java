package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.Rating;
import com.openclassrooms.project.poseidon.repositories.RatingRepository;
import com.openclassrooms.project.poseidon.services.RatingService;
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
public class RatingServiceTest
{
    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingServiceUnderTest;

    private static Rating rating;

    @BeforeAll
    static void setUp( )
    {
        rating = new Rating( );
        rating.setId( 1 );
        rating.setMoodysRating( "AAA" );
        rating.setFitchRating( "BBB" );
        rating.setSandPRating( "CCC" );
        rating.setOrderNumber( 12345 );
    }

    @Test
    void getAllRatings( )
    {
        // GIVEN
        when( ratingRepository.findAll( ) ).thenReturn( List.of( rating ) );

        // WHEN
        List<Rating> ratingList = ratingServiceUnderTest.getAllRatings( );

        // THEN
        assertThat( ratingList.size( ) ).isEqualTo( 1 );
        assertThat( ratingList.get( 0 ) ).isEqualTo( rating );
    }

    @Test
    void findRatingById( )
    {
        // GIVEN
        when( ratingRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( rating ) );

        // WHEN
        Rating ratingTest = ratingServiceUnderTest.findRatingById( 1 );

        // THEN
        assertThat( ratingTest ).isEqualTo( rating );
    }

    @Test
    void findRatingByIdError( )
    {
        // GIVEN
        when( ratingRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid rating Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> ratingServiceUnderTest.findRatingById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewRatings( )
    {
        // WHEN
        ratingServiceUnderTest.addNewRatings( rating );

        // THEN
        verify( ratingRepository ).save( rating );
    }

    @Test
    void updateRating( )
    {
        // GIVEN
        Rating updatedRating = new Rating( );
        updatedRating.setFitchRating( "DDD" );
        updatedRating.setSandPRating( "EEE" );
        updatedRating.setMoodysRating( "FFF" );
        updatedRating.setOrderNumber( 67890 );

        // WHEN
        ratingServiceUnderTest.updateRating( 1, updatedRating );

        // THEN
        verify( ratingRepository ).save( updatedRating );
        assertThat( updatedRating.getId( ) ).isEqualTo( 1 );
    }

    @Test
    void deleteRating( )
    {
        // GIVEN
        when( ratingRepository.findById( 1 ) ).thenReturn( Optional.of( rating ) );

        // WHEN
        ratingServiceUnderTest.deleteRating( 1 );

        // THEN
        verify( ratingRepository ).delete( rating );
    }
}
