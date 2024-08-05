package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.RuleNameController;
import com.openclassrooms.project.poseidon.domain.RuleName;
import com.openclassrooms.project.poseidon.services.RuleNameService;
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

@WebMvcTest(controllers = RuleNameController.class)
@ExtendWith(MockitoExtension.class)
public class RuleNameControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RuleNameService ruleNameService;

    @InjectMocks
    private RuleNameController ruleNameControllerUnderTest;

    private List<RuleName> ruleList;
    private RuleName validRule;
    private RuleName invalidRule;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock RuleName object
        ruleList = new ArrayList<>();
        validRule = new RuleName( );
        validRule.setId( 1 );
        validRule.setName( "Name Test" );
        validRule.setDescription( "Description Test" );
        validRule.setJson( "Json Test" );
        validRule.setTemplate( "Template Test" );
        validRule.setSqlStr( "SQL Test" );
        validRule.setSqlPart( "SQLPart Test" );
        ruleList.add( validRule );

        invalidRule = new RuleName( );
        invalidRule.setId( 1 );

        // Mock the behavior of ruleNameService.findRulenameById
        when( ruleNameService.findRulenameById( 1 ) ).thenReturn( validRule );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( ruleNameService.getAllRulenames( ) ).thenReturn( ruleList );

        // Act & Assert
        mockMvc.perform( get( "/ruleName/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "ruleNames" ) )
                .andExpect( model( ).attribute( "ruleNames", ruleList ) )
                .andExpect( view( ).name( "ruleName/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddRuleNameFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/ruleName/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "ruleName/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/ruleName/validate" )
                        .flashAttr( "ruleName", validRule )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/ruleName/list" ) );

        // Verify that the addNewRulename service method was called with the correct arguments
        verify( ruleNameService ).addNewRulename( any( RuleName.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/ruleName/validate" )
                        .flashAttr( "ruleName", invalidRule )
                        .flashAttr( MODEL_KEY_PREFIX + "ruleName", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "ruleName", "name", "description", "json", "template", "sqlStr", "sqlPart" ) )
                .andExpect( view( ).name( "ruleName/add" ) );

        // Verify that the addNewRulename service method was NOT called
        verify( ruleNameService, never( ) ).addNewRulename( any( RuleName.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/ruleName/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "ruleName" ) )
                .andExpect( model( ).attribute( "ruleName", validRule ) )
                .andExpect( view( ).name( "ruleName/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateRuleNameSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/ruleName/update/1" )
                        .flashAttr( "ruleName", validRule )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/ruleName/list" ) );

        // Verify that the updateRulename service method was called with the correct arguments
        verify( ruleNameService ).updateRulename( any( RuleName.class ), eq( 1 ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateRuleNameError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/ruleName/update/1" )
                        .flashAttr( "ruleName", invalidRule )
                        .flashAttr( MODEL_KEY_PREFIX + "ruleName", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "ruleName", "name", "description", "json", "template", "sqlStr", "sqlPart" ) )
                .andExpect( view( ).name( "ruleName/update" ) );

        // Verify that the updateRulename service method was NOT called
        verify( ruleNameService, never( ) ).updateRulename( any( RuleName.class ), anyInt( ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteRuleNameSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/ruleName/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/ruleName/list" ) );
    }
}
