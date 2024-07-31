package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.BidList;
import com.openclassrooms.project.poseidon.services.BidListService;
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
public class BidListController
{
    private final BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "bidLists", bidListService.getAllBids( ) );

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm( BidList bid )
    {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate( @Valid BidList bid, BindingResult result, Model model )
    {
        // Checks that the data is valid according to the bidlist bean validation annotations
        if ( result.hasErrors( ) )
        {
            return "bidList/add";
        }

        // If data is valid, save new bid to db
        bidListService.addNewBid( bid );

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer bidId, Model model )
    {
        BidList bidToUpdate = bidListService.findBidById( bidId );
        model.addAttribute( "bidList", bidToUpdate );

        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid( @PathVariable ("id" ) Integer bidId, @Valid BidList bidList, BindingResult result, Model model )
    {
        // Checks that the data is valid according to the bidList bean validation annotations
        if ( result.hasErrors( ) )
        {
            return "bidList/update";
        }

        // If data is valid, update bid in db
        bidListService.updateBid( bidId, bidList );

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable( "id" ) Integer bidId, Model model )
    {
        // Finds the bid by Id and delete the bid
        bidListService.deleteBid( bidId );

        // Returns to bid list
        return "redirect:/bidList/list";
    }
}
