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

    public List<CurvePoint> getAllCurvePoints( )
    {
        List<CurvePoint> allCurvepointsList = new ArrayList<>( );
        curvePointRepository.findAll( ).forEach( allCurvepointsList::add );

        return allCurvepointsList;
    }

    public CurvePoint findCurvepointById( Integer curveId )
    {
        return curvePointRepository.findById( curveId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid curve Id:" + curveId ) );
    }

    @Transactional
    public void addNewCurvepoint( CurvePoint curvePoint )
    {
        Timestamp creationDate = new Timestamp( System.currentTimeMillis( ) );
        curvePoint.setCreationDate( creationDate );
        curvePointRepository.save( curvePoint );
    }

    @Transactional
    public void updateCurvepoint( Integer curveId, CurvePoint curvePoint )
    {
        Timestamp updateDate = new Timestamp( System.currentTimeMillis( ) );
        curvePoint.setAsOfDate( updateDate );
        curvePoint.setId( curveId );
        curvePointRepository.save( curvePoint );
    }

    @Transactional
    public void deleteCurvepoint( Integer curveId )
    {
        CurvePoint curvePointToDelete = findCurvepointById( curveId );
        curvePointRepository.delete( curvePointToDelete );
    }
}
