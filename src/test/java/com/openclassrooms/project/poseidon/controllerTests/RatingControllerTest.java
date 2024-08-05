package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.RatingController;
import com.openclassrooms.project.poseidon.domain.Rating;
import com.openclassrooms.project.poseidon.services.RatingService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@WebMvcTest(controllers = RatingController.class)
@ExtendWith(MockitoExtension.class)
public class RatingControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingControllerUnderTest;

    private List<Rating> ratingList;
    private Rating validRating;
    private Rating invalidRating;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock Rating object
        ratingList = new ArrayList<>();
        validRating = new Rating( );
        validRating.setId( 1 );
        validRating.setSandPRating( "AAA" );
        validRating.setFitchRating( "Aaa" );
        validRating.setMoodysRating( "Aaa" );
        validRating.setOrderNumber( 2 );
        ratingList.add( validRating );

        invalidRating = new Rating( );
        invalidRating.setId( 1 );

        // Mock the behavior of ratingService.findRatingById
        when( ratingService.findRatingById( 1 ) ).thenReturn( validRating );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( ratingService.getAllRatings( ) ).thenReturn( ratingList );

        // Act & Assert
        mockMvc.perform( get( "/rating/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "ratings" ) )
                .andExpect( model( ).attribute( "ratings", ratingList ) )
                .andExpect( view( ).name( "rating/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddRatingFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/rating/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "rating/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/rating/validate" )
                        .flashAttr( "rating", validRating )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/rating/list" ) );

        // Verify that the addNewRatings service method was called with the correct arguments
        verify( ratingService ).addNewRatings( any( Rating.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/rating/validate" )
                        .flashAttr( "rating", invalidRating )
                        .flashAttr( MODEL_KEY_PREFIX + "rating", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "rating", "moodysRating", "sandPRating", "fitchRating", "orderNumber" ) )
                .andExpect( view( ).name( "rating/add" ) );

        // Verify that the addNewRatings service method was NOT called
        verify( ratingService, never( ) ).addNewRatings( any( Rating.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/rating/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "rating" ) )
                .andExpect( model( ).attribute( "rating", validRating ) )
                .andExpect( view( ).name( "rating/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateRatingSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/rating/update/1" )
                        .flashAttr( "rating", validRating )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/rating/list" ) );

        // Verify that the updateRating service method was called with the correct arguments
        verify( ratingService ).updateRating( eq( 1 ), any( Rating.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateRatingError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/rating/update/1" )
                        .flashAttr( "rating", invalidRating )
                        .flashAttr( MODEL_KEY_PREFIX + "rating", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "rating", "moodysRating", "sandPRating", "fitchRating", "orderNumber" ) )
                .andExpect( view( ).name( "rating/update" ) );

        // Verify that the updateRating service method was NOT called
        verify( ratingService, never( ) ).updateRating( anyInt( ), any( Rating.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteRatingSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/rating/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/rating/list" ) );
    }
}
