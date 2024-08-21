package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.UserController;
import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.domain.dto.UserDTO;
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
    private UserDTO validUserDTO;
    private UserDTO invalidUserDTO;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup( this.context )
                .apply( springSecurity( ) )
                .build( );

        // Initialize a mock User object
        userList = new ArrayList<>();
        User validUser = new User( );
        validUser.setId( 1 );
        validUser.setUsername( "Username Test" );
        validUser.setPassword( "Password4test!" );
        validUser.setRole( "Admin" );
        userList.add( validUser );

        // Initialize a mock UserDTO object
        validUserDTO = new UserDTO( );
        validUserDTO.setId( validUser.getId( ) );
        validUserDTO.setUsername( validUser.getUsername( ) );
        validUserDTO.setPassword( validUser.getPassword( ) );
        validUserDTO.setRole( validUser.getRole( ) );

        invalidUserDTO = new UserDTO( );
        invalidUserDTO.setId( 1 );
        invalidUserDTO.setPassword( "Badpassword" );

        // Mock the behavior of userService.findUserById
        when( userService.findUserById( 1 ) ).thenReturn( validUser );
        when( userService.getUserDTO( 1 ) ).thenReturn( validUserDTO );
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
                        .flashAttr( "userDTO", validUserDTO )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/user/list" ) );

        // Verify that the addNewUser service method was called with the correct arguments
        verify( userService ).addNewUser( any( UserDTO.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doValidateError( ) throws Exception
    {
        // Mock the BindingResult to simulate validation errors
        BindingResult result = mock( BindingResult.class );

        // Act & Assert
        mockMvc.perform( post( "/user/validate" )
                        .flashAttr( "userDTO", invalidUserDTO )
                        .flashAttr( MODEL_KEY_PREFIX + "userDTO", result ) // Attach the mocked BindingResult
                        .with( csrf( ) ) )
                .andExpect( status( ).isOk( ) )  // Expect HTTP 200 status (stay on the same page)
                .andExpect( model( ).attributeHasFieldErrors( "userDTO", "username", "password" ) )
                .andExpect( view( ).name( "user/add" ) );

        // Verify that the addNewUser service method was NOT called
        verify( userService, never( ) ).addNewUser( any( UserDTO.class ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewShowUpdateFormSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/user/update/1" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "user" ) )
                .andExpect( model( ).attribute( "user", validUserDTO ) )
                .andExpect( view( ).name( "user/update" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void doUpdateUserSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( post( "/user/update/1" )
                        .flashAttr( "userDTO", validUserDTO )
                        .with( csrf( ) ) )
                .andExpect( status( ).is3xxRedirection( ) )
                .andExpect( redirectedUrl( "/user/list" ) );

        // Verify that the updateUser service method was called with the correct arguments
        verify( userService ).updateUser( eq( 1 ), any( UserDTO.class ) );
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
