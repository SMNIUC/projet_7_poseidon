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

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig
{
    @Bean
    public UserDetailsService userDetailsService( UserRepository userRepository ) {
        return new UserDetailsService( userRepository );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder( ) {
        return new BCryptPasswordEncoder( );
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository( ) {
        return new InMemoryTokenRepositoryImpl( );
    }

    // FilterChain for the AUTHZ portion
    // method to push all HTTP requests through the security filter chain and configure the default login page
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http, UserRepository userRepository ) throws Exception
    {
        http
                .csrf( AbstractHttpConfigurer::disable )
                .authorizeHttpRequests( authorizeHttpRequests -> //means the requests are going to be authorized thru the following filters/Matchers
                        authorizeHttpRequests
                                .requestMatchers( "/registration" ).permitAll( )
                                .anyRequest( ).authenticated( ) //so the form below will be used for authentication + ensures that all requests that are not authenticated get a 401 Error
                )
                .formLogin(formLogin -> formLogin
                        .loginPage( "/login" ) // only page(s) available without being logged in
                        .failureUrl("/berror")
                        .defaultSuccessUrl( "/", true )
                        .permitAll( ) )
                .logout( LogoutConfigurer::permitAll )
                .rememberMe( rememberMe -> rememberMe
                        .tokenRepository( persistentTokenRepository( ) )
                        .tokenValiditySeconds( 86400 ) // 24 hours
                        .key( "mySecretKey" ) )
                .userDetailsService( userDetailsService( userRepository ) )
        ;

        return http.build( );
    }
}
