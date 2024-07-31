package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserRepository userRepository;

    public List<User> getAllUsers( )
    {
        List<User> allUsersList = new ArrayList<>( );
        userRepository.findAll( ).forEach( allUsersList::add );

        return allUsersList;
    }

    public User findUserById( Integer userId )
    {
        return userRepository.findById( userId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid user Id:" + userId ) );
    }

    @Transactional
    public void addNewUser( User user )
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( );
        user.setPassword( encoder.encode( user.getPassword( ) ) );
        userRepository.save( user );
    }

    @Transactional
    public void updateUser( Integer userId, User user )
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( );
        user.setPassword( encoder.encode( user.getPassword( ) ) );
        user.setId( userId );
        userRepository.save( user );
    }

    @Transactional
    public void deleteUser( Integer userId )
    {
        User userToDelete = findUserById( userId );
        userRepository.delete( userToDelete );
    }
}
