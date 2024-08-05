package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.UserController;
import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.services.UserService;
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

@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userControllerUnderTest;

    private List<User> userList;
    private User validUser;
    private User invalidUser;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock User object
        userList = new ArrayList<>();
        validUser = new User( );
        validUser.setId( 1 );
        validUser.setUsername( "Username Test" );
        validUser.setPassword( "Password4test!" );
        validUser.setRole( "Admin" );
        userList.add( validUser );

        invalidUser = new User( );
        invalidUser.setId( 1 );
        invalidUser.setPassword( "Badpassword" );

        // Mock the behavior of userService.findUserById
        when( userService.findUserById( 1 ) ).thenReturn( validUser );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewHomePageSuccessful( ) throws Exception
    {
        // Arrange
        when( userService.getAllUsers( ) ).thenReturn( userList );

        // Act & Assert
        mockMvc.perform( get( "/user/list" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "httpServletRequest" ) )
                .andExpect( model( ).attributeExists( "users" ) )
                .andExpect( model( ).attribute( "users", userList ) )
                .andExpect( view( ).name( "user/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewAddUserSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/user/add" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "user/add" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/user/validate" )
                        .flashAttr( "user", validUser )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/user/list" ) );

        // Verify that the addNewUser service method was called with the correct arguments
        verify( userService ).addNewUser( any( User.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/user/validate" )
                        .flashAttr( "user", invalidUser )
                        .flashAttr( MODEL_KEY_PREFIX + "user", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "user", "username", "password" ) )
                .andExpect( view( ).name( "user/add" ) );

        // Verify that the addNewUser service method was NOT called
        verify( userService, never( ) ).addNewUser( any( User.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/user/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "user" ) )
                .andExpect( model( ).attribute( "user", validUser ) )
                .andExpect( view( ).name( "user/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateUserSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/user/update/1" )
                        .flashAttr( "user", validUser )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/user/list" ) );

        // Verify that the updateUser service method was called with the correct arguments
        verify( userService ).updateUser( eq( 1 ), any( User.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateUserError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        // Perform POST request with invalid data
        mockMvc.perform( post( "/user/update/1" )
                        .flashAttr( "user", invalidUser )
                        .flashAttr( MODEL_KEY_PREFIX + "user", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "user", "username", "password" ) )
                .andExpect( view( ).name( "user/update" ) );

        // Verify that the updateUser service method was NOT called
        verify( userService, never( ) ).updateUser( anyInt( ), any( User.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doDeleteUserSuccessful( ) throws Exception
    {
        mockMvc.perform( get( "/user/delete/1" ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/user/list" ) );
    }
}
