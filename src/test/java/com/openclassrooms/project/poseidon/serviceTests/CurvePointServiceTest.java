package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.CurvePoint;
import com.openclassrooms.project.poseidon.repositories.CurvePointRepository;
import com.openclassrooms.project.poseidon.services.CurvePointService;
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
public class CurvePointServiceTest
{
    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointService curvePointServiceUnderTest;

    private static CurvePoint curve;

    @BeforeAll
    static void setUp( )
    {
        curve = new CurvePoint( );
        curve.setId( 1 );
        curve.setCurveId( 2 );
        curve.setTerm( 3d );
        curve.setValue( 4d );
    }

    @Test
    void getAllCurvePoints( )
    {
        // GIVEN
        when( curvePointRepository.findAll( ) ).thenReturn( List.of( curve ) );

        // WHEN
        List<CurvePoint> curvePointList = curvePointServiceUnderTest.getAllCurvePoints( );

        // THEN
        assertThat( curvePointList.size( ) ).isEqualTo( 1 );
        assertThat( curvePointList.get( 0 ) ).isEqualTo( curve );
    }

    @Test
    void findCurvepointById( )
    {
        // GIVEN
        when( curvePointRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( curve ) );

        // WHEN
        CurvePoint curvePoint = curvePointServiceUnderTest.findCurvepointById( 1 );

        // THEN
        assertThat( curvePoint ).isEqualTo( curve );
    }

    @Test
    void findCurvePointByIdError( )
    {
        // GIVEN
        when( curvePointRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid curve Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> curvePointServiceUnderTest.findCurvepointById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewCurvepoint( )
    {
        // WHEN
        curvePointServiceUnderTest.addNewCurvepoint( curve );

        // THEN
        verify( curvePointRepository ).save( curve );
        assertThat( curve.getCreationDate( ) ).isNotNull( );
    }

    @Test
    void updateCurvepoint( )
    {
        // GIVEN
        CurvePoint updatedCurve = new CurvePoint( );
        updatedCurve.setCurveId( 3 );
        updatedCurve.setTerm( 4d );
        updatedCurve.setValue( 5d );

        // WHEN
        curvePointServiceUnderTest.updateCurvepoint( 1, updatedCurve );

        // THEN
        verify( curvePointRepository ).save( updatedCurve );
        assertThat( updatedCurve.getAsOfDate( ) ).isNotNull( );
        assertThat( updatedCurve.getId( ) ).isEqualTo( 1 );
    }

    @Test
    void deleteCurvepoint( )
    {
        // GIVEN
        when( curvePointRepository.findById( 1 ) ).thenReturn( Optional.of( curve ) );

        // WHEN
        curvePointServiceUnderTest.deleteCurvepoint( 1 );

        // THEN
        verify( curvePointRepository ).delete( curve );
    }
}
