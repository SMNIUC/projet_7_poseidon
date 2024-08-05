package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.repositories.UserRepository;
import com.openclassrooms.project.poseidon.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userServiceUnderTest;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    private static User user;

    @BeforeAll
    static void setUp( )
    {
        user = new User( );
        user.setId( 1 );
        user.setUsername( "Username" );
        user.setPassword( "Password!1" );
        user.setRole( "ADMIN" );
    }

    @Test
    void getAllUsers( )
    {
        // GIVEN
        when( userRepository.findAll( ) ).thenReturn( List.of( user ) );

        // WHEN
        List<User> userList = userServiceUnderTest.getAllUsers( );

        // THEN
        assertThat( userList.size( ) ).isEqualTo( 1 );
        assertThat( userList.get( 0 ) ).isEqualTo( user );
    }

    @Test
    void findUserById( )
    {
        // GIVEN
        when( userRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( user ) );

        // WHEN
        User userTest = userServiceUnderTest.findUserById( 1 );

        // THEN
        assertThat( userTest ).isEqualTo( user );
    }

    @Test
    void findUserByIdError( )
    {
        // GIVEN
        when( userRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid user Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> userServiceUnderTest.findUserById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewUser( )
    {
        // WHEN
        userServiceUnderTest.addNewUser( user );

        // THEN
        verify( userRepository ).save( user );
        verify( passwordEncoder ).encode( "Password!1" );
        assertThat( "Password!1" ).isNotEqualTo( user.getPassword( ) );
    }

    @Test
    void updateUser( )
    {
        // GIVEN
        User updatedUser = new User( );
        updatedUser.setPassword( "newPassword2@" );

        // WHEN
        userServiceUnderTest.updateUser( 1, updatedUser );

        // THEN
        verify( userRepository ).save( updatedUser );
        assertThat( updatedUser.getId( ) ).isEqualTo( 1 );
        verify( passwordEncoder ).encode( "newPassword2@" );
        assertThat( "newPassword2@" ).isNotEqualTo( user.getPassword( ) );
    }

    @Test
    void deleteUser( )
    {
        // GIVEN
        when( userRepository.findById( 1 ) ).thenReturn( Optional.of( user ) );

        // WHEN
        userServiceUnderTest.deleteUser( 1 );

        // THEN
        verify( userRepository ).delete( user );
    }
}
