package com.openclassrooms.project.poseidon.controllerTests;

import com.openclassrooms.project.poseidon.controllers.LoginController;
import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LoginController.class)
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup( )
    {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup( new LoginController( userService ) )
                .setViewResolvers( viewResolver( ) )
                .build( );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewLoginSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/login" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( view( ).name( "login" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void getAllUserArticlesSuccessful( ) throws Exception
    {
        // Arrange - Setup mock repository behavior
        User user1 = new User( );
        user1.setId( 1 );
        user1.setUsername( "user1" );

        User user2 = new User( );
        user2.setId( 2 );
        user2.setUsername( "user2" );

        when( userService.getAllUsers( ) ).thenReturn( Arrays.asList( user1, user2 ) );

        // Act & Assert
        mockMvc.perform( get( "/secure/article-details" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "users" ) )
                .andExpect( model( ).attribute( "users", Arrays.asList( user1, user2 ) ) )
                .andExpect( view( ).name( "user/list" ) );
    }

    @Test
    @WithMockUser( username = "admin", roles = {"admin"} )
    void viewErrorSuccessful( ) throws Exception
    {
        // Act & Assert
        mockMvc.perform( get( "/berror" ) )
                .andExpect( status( ).isOk( ) )
                .andExpect( model( ).attributeExists( "errorMsg" ) )
                .andExpect( view( ).name("403" ) );
    }

    // Define a ViewResolver bean for the test context
    private ViewResolver viewResolver( )
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver( );
        viewResolver.setPrefix( "/WEB-INF/views/" );
        viewResolver.setSuffix( ".jsp" );

        return viewResolver;
    }
}
