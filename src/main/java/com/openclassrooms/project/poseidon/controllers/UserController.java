package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.User;
import com.openclassrooms.project.poseidon.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @RequestMapping("/user/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "users", userService.getAllUsers( ) );
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser( User user )
    {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate( @Valid User user, BindingResult result )
    {
        if ( !result.hasErrors( ) )
        {
            userService.addNewUser( user );

            return "redirect:/user/list";
        }

        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer userId, Model model )
    {
        User user = userService.findUserById( userId );
        user.setPassword( "" );
        model.addAttribute( "user", user );

        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser( @PathVariable( "id" ) Integer userId, @Valid User user, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "user/update";
        }

        userService.updateUser( userId, user );

        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser( @PathVariable( "id" ) Integer userId )
    {
        userService.deleteUser( userId );

        return "redirect:/user/list";
    }
}
