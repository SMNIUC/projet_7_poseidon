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

    /**
     * Gets all the available ratings from the database
     *
     * @return a list of ratings
     */
    public List<Rating> getAllRatings( )
    {
        List<Rating> allRatingsList = new ArrayList<>( );
        ratingRepository.findAll( ).forEach( allRatingsList::add );

        return allRatingsList;
    }

    /**
     * Finds a rating object in the database from its id
     *
     * @param ratingId the id of the rating object to be found
     * @return a rating object
     */
    public Rating findRatingById( Integer ratingId )
    {
        return ratingRepository.findById( ratingId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid rating Id:" + ratingId ) );
    }

    /**
     * Creates and saves a new rating in the database
     *
     * @param rating the rating to be saved
     */
    @Transactional
    public void addNewRatings( Rating rating )
    {
        ratingRepository.save( rating );
    }

    /**
     * Updates a rating object in the database with new info
     *
     * @param ratingId the id of the rating object to be updated
     * @param rating a rating object that holds the new ratings information
     */
    @Transactional
    public void updateRating( Integer ratingId, Rating rating )
    {
        rating.setId( ratingId );
        ratingRepository.save( rating );
    }

    /**
     * Deletes a rating object in the database, based on its id
     *
     * @param ratingId the id of the rating object to be deleted
     */
    @Transactional
    public void deleteRating( Integer ratingId )
    {
        Rating ratingToDelete = findRatingById( ratingId );
        ratingRepository.delete( ratingToDelete );
    }
}
