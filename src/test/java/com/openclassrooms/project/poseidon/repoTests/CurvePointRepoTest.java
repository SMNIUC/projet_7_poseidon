package com.openclassrooms.project.poseidon.repoTests;

import com.openclassrooms.project.poseidon.domain.CurvePoint;
import com.openclassrooms.project.poseidon.repositories.CurvePointRepository;
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
public class CurvePointRepoTest
{
    @Autowired
    private CurvePointRepository curvePointRepository;

    @Test
    public void curvePointTest( )
    {
        CurvePoint curvePoint = new CurvePoint( );
        curvePoint.setCurveId( 10 );
        curvePoint.setTerm( 10d );
        curvePoint.setValue( 30d );

        // Save
        curvePoint = curvePointRepository.save(curvePoint);
        Assert.assertNotNull(curvePoint.getId());
        Assert.assertEquals( 10, ( int ) curvePoint.getCurveId( ) );

        // Update
        curvePoint.setCurveId(20);
        curvePoint = curvePointRepository.save(curvePoint);
        Assert.assertEquals( 20, ( int ) curvePoint.getCurveId( ) );

        // Find
        List<CurvePoint> listResult = ( List<CurvePoint> ) curvePointRepository.findAll();
        Assert.assertFalse( listResult.isEmpty( ) );

        // Delete
        Integer id = curvePoint.getId();
        curvePointRepository.delete(curvePoint);
        Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
        Assert.assertFalse(curvePointList.isPresent());
    }
}
