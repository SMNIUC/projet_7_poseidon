package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.Rating;
import com.openclassrooms.project.poseidon.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RatingService
{
    private final RatingRepository ratingRepository;

    public List<Rating> getAllRatings( )
    {
        List<Rating> allRatingsList = new ArrayList<>( );
        ratingRepository.findAll( ).forEach( allRatingsList::add );
        return allRatingsList;
    }

    public Rating findRatingById( Integer ratingId )
    {
        return ratingRepository.getRatingById( ratingId );
    }

    @Transactional
    public void addNewRatings( Rating rating )
    {
        ratingRepository.save( rating );
    }

    @Transactional
    public void deleteRating( Integer ratingId )
    {
        Rating ratingToDelete = findRatingById( ratingId );
        ratingRepository.delete( ratingToDelete );
    }

    @Transactional
    public void updateRating( Integer ratingId, Rating rating )
    {
        Rating ratingToUpdate = findRatingById( ratingId );
        ratingToUpdate.setSandPRating( rating.getSandPRating( ) );
        ratingToUpdate.setFitchRating( rating.getFitchRating( ) );
        ratingToUpdate.setMoodysRating( rating.getMoodysRating( ) );
        ratingToUpdate.setOrderNumber( rating.getOrderNumber( ) );
        ratingRepository.save( ratingToUpdate );
    }
}
