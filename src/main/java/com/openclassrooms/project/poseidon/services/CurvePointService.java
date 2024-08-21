package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.CurvePoint;
import com.openclassrooms.project.poseidon.repositories.CurvePointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CurvePointService
{
    private final CurvePointRepository curvePointRepository;

    /**
     * Gets all the available curve points from the database
     *
     * @return a list of curve points
     */
    public List<CurvePoint> getAllCurvePoints( )
    {
        List<CurvePoint> allCurvepointsList = new ArrayList<>( );
        curvePointRepository.findAll( ).forEach( allCurvepointsList::add );

        return allCurvepointsList;
    }

    /**
     * Finds a curve point in the database from its id
     *
     * @param curveId the id of the curve point to be found
     * @return a curve point
     */
    public CurvePoint findCurvepointById( Integer curveId )
    {
        return curvePointRepository.findById( curveId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid curve Id:" + curveId ) );
    }

    /**
     * Creates and saves a new curve point in the database
     *
     * @param curvePoint the curve point to be saved
     */
    @Transactional
    public void addNewCurvepoint( CurvePoint curvePoint )
    {
        Timestamp creationDate = new Timestamp( System.currentTimeMillis( ) );
        curvePoint.setCreationDate( creationDate );
        curvePointRepository.save( curvePoint );
    }

    /**
     * Updates a curve point in the database with new info
     *
     * @param curveId the id of the curve point to be updated
     * @param curvePoint a curvePoint object that holds the new curve point information
     */
    @Transactional
    public void updateCurvepoint( Integer curveId, CurvePoint curvePoint )
    {
        Timestamp updateDate = new Timestamp( System.currentTimeMillis( ) );
        curvePoint.setAsOfDate( updateDate );
        curvePoint.setId( curveId );
        curvePointRepository.save( curvePoint );
    }

    /**
     * Deletes a curve point in the database, based on its id
     *
     * @param curveId the id of the curve point to be deleted
     */
    @Transactional
    public void deleteCurvepoint( Integer curveId )
    {
        CurvePoint curvePointToDelete = findCurvepointById( curveId );
        curvePointRepository.delete( curvePointToDelete );
    }
}
