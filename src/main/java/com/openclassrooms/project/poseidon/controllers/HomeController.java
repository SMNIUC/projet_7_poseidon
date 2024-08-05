package com.openclassrooms.project.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class HomeController
{
    @RequestMapping("/")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute("httpServletRequest", request);
        return "home";
    }
}
