package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.BidListController;
import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.services.BidListService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.validation.BindingResult.MODEL_KEY_PREFIX;

@WebMvcTest(controllers = BidListController.class)
@ExtendWith(MockitoExtension.class)
class BidListControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BidListService bidListService;

    @InjectMocks
    private BidListController bidListControllerUnderTest;

    private List<BidList> bidList;
    private BidList validBid;
    private BidList invalidBid;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock BidList object
        bidList = new ArrayList<>();
        validBid = new BidList( );
        validBid.setBidListId( 1 );
        validBid.setAccount( "Account Test" );
        validBid.setType( "Account Type" );
        validBid.setBidQuantity( 10d );
        bidList.add( validBid );

        invalidBid = new BidList( );
        invalidBid.setBidListId( 1 );

        // Mock the behavior of bidListService.findBidById
        when( bidListService.findBidById( 1 ) ).thenReturn( validBid );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( bidListService.getAllBids( ) ).thenReturn( bidList );

        // Act & Assert
        mockMvc.perform( get( "/bidList/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "bidLists" ) )
                .andExpect( model( ).attribute( "bidLists", bidList ) )
                .andExpect( view( ).name( "bidList/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddBidFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/bidList/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "bidList/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/bidList/validate" )
                        .flashAttr( "bidList", validBid )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/bidList/list" ) );

        // Verify that the addNewBid service method was called with the correct arguments
        verify( bidListService ).addNewBid( any( BidList.class ) ) ;
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/bidList/validate" )
                        .flashAttr( "bidList", invalidBid )
                        .flashAttr( MODEL_KEY_PREFIX + "bidList", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "bidList", "account", "type", "bidQuantity" ) )
                .andExpect( view( ).name( "bidList/add" ) );

        // Verify that the addNewBid service method was NOT called
        verify( bidListService, never( ) ).addNewBid( any( BidList.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/bidList/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "bidList" ) )
                .andExpect( model( ).attribute( "bidList", validBid ) )
                .andExpect( view( ).name( "bidList/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateBidSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/bidList/update/1" )
                        .flashAttr( "bidList", validBid )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/bidList/list" ) );

        // Verify that the updateBid service method was called with the correct arguments
        verify( bidListService ).updateBid( eq( 1 ), any( BidList.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateBidError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/bidList/update/1" )
                        .flashAttr( "bidList", invalidBid )
                        .flashAttr( MODEL_KEY_PREFIX + "bidList", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "bidList", "account", "type", "bidQuantity" ) )
                .andExpect( view( ).name( "bidList/update" ) );

        // Verify that the updateBid service method was NOT called
        verify( bidListService, never( ) ).updateBid( anyInt( ), any( BidList.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteBidSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/bidList/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/bidList/list" ) );
    }
}
