package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.config.MyUserDetails;
import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user-specific data.
 *
 * <p>
 * This service is used by Spring Security to load user details during the authentication process.
 * It retrieves the user information from the {@link UserRepository} based on the provided email (username).
 * </p>
 *
 * <p>
 * The {@code UserDetailsService} is annotated with {@link Service} to indicate that it's a Spring service component.
 * The class is also annotated with {@link RequiredArgsConstructor}, which generates a constructor for the final fields,
 * in this case, {@code userRepository}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{
    private final UserRepository userRepository;

    /**
     * Loads the user's details by their username (email) for authentication.
     *
     * <p>
     * This method is used by Spring Security during the authentication process to fetch user details
     * from the database. If the user is not found, a {@link BadCredentialsException} is thrown.
     * </p>
     *
     * @param email the email (username) of the user trying to authenticate
     * @return the {@link UserDetails} containing the user's information
     * @throws BadCredentialsException if no user is found with the provided email
     */
    @Override
    public UserDetails loadUserByUsername( String email ) throws BadCredentialsException
    {
        User user = userRepository.findByUsername( email );

        if( user == null )
        {
            throw new BadCredentialsException( "Could not find user" );
        }

        return new MyUserDetails( user );
    }
}
