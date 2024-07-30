package com.openclassrooms.project.poseidon.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController
{
    @RequestMapping("/")
    public String home( )
    {
        return "home";
    }

    @RequestMapping("/admin/home")
    public String adminHome( )
    {
        return "redirect:/bidList/list";
    }
}
