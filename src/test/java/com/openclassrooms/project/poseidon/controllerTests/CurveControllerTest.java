package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.CurveController;
import com.openclassrooms.project.poseidon.domain.CurvePoint;
import com.openclassrooms.project.poseidon.services.CurvePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@WebMvcTest(controllers = CurveController.class)
@ExtendWith(MockitoExtension.class)
public class CurveControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private CurvePointService curvePointService;

    @InjectMocks
    private CurveController curveControllerUnderTest;

    private List<CurvePoint> curvePointList;
    private CurvePoint validCurve;
    private CurvePoint invalidCurve;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock Curvepoint object
        curvePointList = new ArrayList<>();
        validCurve = new CurvePoint( );
        validCurve.setId( 1 );
        validCurve.setCurveId( 2 );
        validCurve.setTerm( 3d );
        validCurve.setValue( 4d );
        curvePointList.add( validCurve );

        invalidCurve = new CurvePoint( );
        invalidCurve.setId( 1 );

        // Mock the behavior of curvePointService.findCurvepointById
        when( curvePointService.findCurvepointById( 1 ) ).thenReturn( validCurve );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( curvePointService.getAllCurvePoints( ) ).thenReturn( curvePointList );

        // Act & Assert
        mockMvc.perform( get( "/curvePoint/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "curvePoints" ) )
                .andExpect( model( ).attribute( "curvePoints", curvePointList ) )
                .andExpect( view( ).name( "curvePoint/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddCurvepointFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/curvePoint/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "curvePoint/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/curvePoint/validate" )
                        .flashAttr( "curvePoint", validCurve )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/curvePoint/list" ) );

        // Verify that the addNewCurvepoint service method was called with the correct arguments
        verify( curvePointService ).addNewCurvepoint( any( CurvePoint.class ) ) ;
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/curvePoint/validate" )
                        .flashAttr( "curvePoint", invalidCurve )
                        .flashAttr( MODEL_KEY_PREFIX + "curvePoint", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "curvePoint", "curveId", "term", "value" ) )
                .andExpect( view( ).name( "curvePoint/add" ) );

        // Verify that the addNewCurvepoint service method was NOT called
        verify( curvePointService, never( ) ).addNewCurvepoint( any( CurvePoint.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/curvePoint/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "curvePoint" ) )
                .andExpect( model( ).attribute( "curvePoint", validCurve ) )
                .andExpect( view( ).name( "curvePoint/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateCurvePointSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/curvePoint/update/1" )
                        .flashAttr( "curvePoint", validCurve )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/curvePoint/list" ) );

        // Verify that the updateCurvepoint service method was called with the correct arguments
        verify( curvePointService ).updateCurvepoint( eq( 1 ), any( CurvePoint.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateCurvepointError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/curvePoint/update/1" )
                        .flashAttr( "curvePoint", invalidCurve )
                        .flashAttr( MODEL_KEY_PREFIX + "curvePoint", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "curvePoint", "curveId", "term", "value" ) )
                .andExpect( view( ).name( "curvePoint/update" ) );

        // Verify that the updateCurvepoint service method was NOT called
        verify( curvePointService, never( ) ).updateCurvepoint( anyInt( ), any( CurvePoint.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteCurvepointSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/curvePoint/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/curvePoint/list" ) );
    }
}
