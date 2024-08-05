package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LoginController
{
    private final UserService userService;

    @GetMapping("/login")
    public ModelAndView login( )
    {
        ModelAndView mav = new ModelAndView( );
        mav.setViewName( "login" );
        return mav;
    }

    @GetMapping("/secure/article-details")
    public ModelAndView getAllUserArticles( )
    {
        ModelAndView mav = new ModelAndView( );
        mav.addObject( "users", userService.getAllUsers( ) );
        mav.setViewName( "user/list" );
        return mav;
    }

    @GetMapping("/berror")
    public ModelAndView error( )
    {
        ModelAndView mav = new ModelAndView( );
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject( "errorMsg", errorMessage );
        mav.setViewName( "403" );
        return mav;
    }
}
