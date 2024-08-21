package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.domain.dto.UserDTO;
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

    private final BCryptPasswordEncoder encoder;

    /**
     * Gets all the available users from the database
     *
     * @return a list of users
     */
    public List<User> getAllUsers( )
    {
        List<User> allUsersList = new ArrayList<>( );
        userRepository.findAll( ).forEach( allUsersList::add );

        return allUsersList;
    }

    /**
     * Finds a user in the database from its id
     *
     * @param userId the id of the user to be found
     * @return a user
     */
    public User findUserById( Integer userId )
    {
        return userRepository.findById( userId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid user Id:" + userId ) );
    }

    /**
     * Builds a UserDTO from a User
     *
     * @param userId the id of the user to transform into UserDTO
     * @return a UserDTO
     */
    public UserDTO getUserDTO( Integer userId )
    {
        User user = findUserById( userId );

        UserDTO userDTO = new UserDTO( );
        userDTO.setId( user.getId( ) );
        userDTO.setFullname( user.getFullname( ) );
        userDTO.setUsername( user.getUsername( ) );
        userDTO.setPassword( "" );
        userDTO.setRole( user.getRole( ) );

        return userDTO;
    }

    /**
     * Creates and saves a new user in the database
     *
     * @param userDTO the userDTO to be saved as User
     */
    @Transactional
    public void addNewUser( UserDTO userDTO )
    {
        User user = new User( );
        user.setFullname( userDTO.getFullname( ) );
        user.setUsername( userDTO.getUsername( ) );
        user.setRole( userDTO.getRole( ) );
        user.setPassword( encoder.encode( userDTO.getPassword( ) ) );
        userRepository.save( user );
    }

    /**
     * Updates a user in the database with new info
     *
     * @param userId the id of the user to be updated
     * @param userDTO a userDTO
     *               object that holds the new user information
     */
    @Transactional
    public void updateUser( Integer userId, UserDTO userDTO )
    {
        User user = findUserById( userId );
        user.setId( userId );
        user.setFullname( userDTO.getFullname( ) );
        user.setUsername( userDTO.getUsername( ) );
        user.setPassword( encoder.encode( userDTO.getPassword( ) ) );
        user.setRole( userDTO.getRole( ) );

        userRepository.save( user );
    }

    /**
     * Deletes a user in the database, based on its id
     *
     * @param userId the id of the user to be deleted
     */
    @Transactional
    public void deleteUser( Integer userId )
    {
        User userToDelete = findUserById( userId );
        userRepository.delete( userToDelete );
    }
}
