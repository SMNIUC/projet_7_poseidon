package com.openclassrooms.project.poseidon.repoTests;

import com.openclassrooms.project.poseidon.domain.Rating;
import com.openclassrooms.project.poseidon.repositories.RatingRepository;
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
public class RatingRepoTest
{
    @Autowired
    private RatingRepository ratingRepository;

    @Test
    public void ratingTest( )
    {
        Rating rating = new Rating( );
        rating.setMoodysRating( "Moodys Rating" );
        rating.setFitchRating( "Fitch Rating" );
        rating.setSandPRating( "Sand PRating" );
        rating.setOrderNumber( 10 );

        // Save
        rating = ratingRepository.save(rating);
        Assert.assertNotNull(rating.getId());
        Assert.assertEquals( 10, ( int ) rating.getOrderNumber( ) );

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        Assert.assertEquals( 20, ( int ) rating.getOrderNumber( ) );

        // Find
        List<Rating> listResult = ( List<Rating> ) ratingRepository.findAll();
        Assert.assertFalse( listResult.isEmpty( ) );

        // Delete
        Integer id = rating.getId();
        ratingRepository.delete(rating);
        Optional<Rating> ratingList = ratingRepository.findById(id);
        Assert.assertFalse(ratingList.isPresent());
    }
}
