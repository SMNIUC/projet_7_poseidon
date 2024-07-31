package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.Rating;
import com.openclassrooms.project.poseidon.services.RatingService;
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
public class RatingController
{
    private final RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "ratings", ratingService.getAllRatings( ) );

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm( Rating rating )
    {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate( @Valid Rating rating, BindingResult result, Model model )
    {
        //TODO -> model any use?

        // Checks that the data is valid according to the rating bean validation annotations
        if ( result.hasErrors( ) )
        {
            return "rating/add";
        }

        // If data is valid, save new rating to db
        ratingService.addNewRatings( rating );

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer ratingId, Model model )
    {
        Rating ratingToUpdate = ratingService.findRatingById( ratingId );
        model.addAttribute( "rating", ratingToUpdate );

        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating( @PathVariable( "id" ) Integer ratingId, @Valid Rating rating, BindingResult result, Model model )
    {
        //TODO -> model any use?

        // Checks that the data is valid according to the rating bean validation annotations
        if ( result.hasErrors( ) )
        {
            return "rating/update";
        }

        // If data is valid, update rating in db
        ratingService.updateRating( ratingId, rating );

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating ( @PathVariable( "id" ) Integer ratingId )
    {
        // Finds the Rating by id and delete the Rating
        ratingService.deleteRating( ratingId );

        return "redirect:/rating/list";
    }
}
