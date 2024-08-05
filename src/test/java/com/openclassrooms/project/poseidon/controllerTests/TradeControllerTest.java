package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.TradeController;
import com.openclassrooms.project.poseidon.domain.Trade;
import com.openclassrooms.project.poseidon.services.TradeService;
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

@WebMvcTest(controllers = TradeController.class)
@ExtendWith(MockitoExtension.class)
public class TradeControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TradeService tradeService;

    @InjectMocks
    private TradeController tradeControllerUnderTest;

    private List<Trade> tradeList;
    private Trade validTrade;
    private Trade invalidTrade;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock Trade object
        tradeList = new ArrayList<>();
        validTrade = new Trade( );
        validTrade.setTradeId( 1 );
        validTrade.setAccount( "Account Test" );
        validTrade.setType( "Type Test" );
        validTrade.setBuyQuantity( 10d );
        tradeList.add( validTrade );

        invalidTrade = new Trade( );
        invalidTrade.setTradeId( 1 );

        // Mock the behavior of tradeService.findTradeById
        when( tradeService.findTradeById( 1 ) ).thenReturn( validTrade );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( tradeService.getAllTrades( ) ).thenReturn( tradeList );

        // Act & Assert
        mockMvc.perform( get( "/trade/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "trades" ) )
                .andExpect( model( ).attribute( "trades", tradeList ) )
                .andExpect( view( ).name( "trade/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddTradeFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/trade/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "trade/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/trade/validate" )
                        .flashAttr( "trade", validTrade )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/trade/list" ) );

        // Verify that the addNewTrade service method was called with the correct arguments
        verify( tradeService ).addNewTrade( any( Trade.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/trade/validate" )
                        .flashAttr( "trade", invalidTrade )
                        .flashAttr( MODEL_KEY_PREFIX + "trade", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "trade", "account", "type", "buyQuantity" ) )
                .andExpect( view( ).name( "trade/add" ) );

        // Verify that the addNewTrade service method was NOT called
        verify( tradeService, never( ) ).addNewTrade( any( Trade.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/trade/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "trade" ) )
                .andExpect( model( ).attribute( "trade", validTrade ) )
                .andExpect( view( ).name( "trade/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateTradeSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/trade/update/1" )
                        .flashAttr( "trade", validTrade )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/trade/list" ) );

        // Verify that the updateTrade service method was called with the correct arguments
        verify( tradeService ).updateTrade( eq( 1 ), any( Trade.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateTradeError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/trade/update/1" )
                        .flashAttr( "trade", invalidTrade )
                        .flashAttr( MODEL_KEY_PREFIX + "trade", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "trade", "account", "type", "buyQuantity" ) )
                .andExpect( view( ).name( "trade/update" ) );

        // Verify that the updateTrade service method was NOT called
        verify( tradeService, never( ) ).updateTrade( anyInt( ), any( Trade.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteTradeSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/trade/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/trade/list" ) );
    }
}
