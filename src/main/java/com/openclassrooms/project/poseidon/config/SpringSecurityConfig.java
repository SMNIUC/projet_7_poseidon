package com.openclassrooms.project.poseidon.config;

import com.openclassrooms.project.poseidon.repositories.UserRepository;
import com.openclassrooms.project.poseidon.services.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * <strong>SpringSecurityConfig</strong> is a configuration class that sets up security-related beans and
 * configurations for the application using Spring Security.
 *
 * <p>This class is annotated with {@link Configuration} and {@link EnableWebSecurity} to indicate that it
 * provides configuration for web security. It defines beans for user details service, password encoder,
 * persistent token repository, and the security filter chain.</p>
 *
 * <p>The configurations include disabling CSRF protection, setting up authorization rules, configuring
 * form-based login and logout mechanisms, and integrating custom user details service.</p>
 *
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{
    /**
     * Creates and configures a {@link UserDetailsService} bean that is responsible for loading user-specific data.
     *
     * <p>
     * This method configures a custom {@link UserDetailsService} using a {@link UserRepository} to retrieve
     * user details for authentication purposes.
     * </p>
     *
     * @param userRepository the {@link UserRepository} used to retrieve user information from the database
     * @return a configured instance of {@link UserDetailsService}
     */
    @Bean
    public UserDetailsService userDetailsService( UserRepository userRepository ) {
        return new UserDetailsService( userRepository );
    }

    /**
     * Creates and configures a {@link BCryptPasswordEncoder} bean for encoding user passwords.
     *
     * <p>
     * This method returns an instance of {@link BCryptPasswordEncoder}, which is used to encode user passwords
     * securely before storing them in the database.
     * </p>
     *
     * @return a new instance of {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder( ) {
        return new BCryptPasswordEncoder( );
    }

    /**
     * Creates and configures a {@link PersistentTokenRepository} bean for managing "remember me" functionality tokens.
     *
     * <p>
     * This method returns an instance of {@link InMemoryTokenRepositoryImpl}, which stores persistent login tokens
     * in memory. This allows users to stay logged in between sessions.
     * </p>
     *
     * @return a new instance of {@link InMemoryTokenRepositoryImpl}
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository( ) {
        return new InMemoryTokenRepositoryImpl( );
    }

    /**
     * Configures the {@link SecurityFilterChain} that defines security policies for HTTP requests.
     *
     * <p>This method sets up various security aspects including disabling CSRF protection, defining authorization
     * rules for HTTP requests, configuring form-based login and logout, and setting up the custom user details service.</p>
     *
     * @param http           the {@link HttpSecurity} object to configure security behaviors
     * @param userRepository the {@link UserRepository} used by the user details service for fetching user data
     * @return a fully configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs while building the security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http, UserRepository userRepository ) throws Exception
    {
        http
                .csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests( authorizeHttpRequests -> //means the requests are going to be authorized thru the following filters/Matchers
                        authorizeHttpRequests
                                .anyRequest( ).authenticated( ) //so the form below will be used for authentication + ensures that all requests that are not authenticated get a 401 Error
                )
                .formLogin(formLogin -> formLogin
                        .loginPage( "/login" ) // only page(s) available without being logged in
                        .defaultSuccessUrl( "/", true )
                        .permitAll( ) )
                .logout( LogoutConfigurer::permitAll )
                .userDetailsService( userDetailsService( userRepository ) )
        ;

        return http.build( );
    }
}
