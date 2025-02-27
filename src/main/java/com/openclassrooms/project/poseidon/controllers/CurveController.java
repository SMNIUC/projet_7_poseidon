package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.CurvePoint;
import com.openclassrooms.project.poseidon.services.CurvePointService;
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
public class CurveController
{
    private final CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "curvePoints", curvePointService.getAllCurvePoints( ) );

        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvepointForm( CurvePoint curvePoint )
    {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate( @Valid CurvePoint curvePoint, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "curvePoint/add";
        }

        curvePointService.addNewCurvepoint( curvePoint );

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer curveId, Model model )
    {
        CurvePoint curvePointToUpdate = curvePointService.findCurvepointById( curveId );
        model.addAttribute( "curvePoint", curvePointToUpdate );

        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvepoint( @PathVariable( "id" ) Integer curveId, @Valid CurvePoint curvePoint, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "curvePoint/update";
        }

        curvePointService.updateCurvepoint( curveId, curvePoint );

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvepoint(@PathVariable( "id" ) Integer curveId )
    {
        curvePointService.deleteCurvepoint( curveId );

        return "redirect:/curvePoint/list";
    }
}
