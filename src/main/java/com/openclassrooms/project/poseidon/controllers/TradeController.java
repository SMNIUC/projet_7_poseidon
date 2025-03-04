package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.Trade;
import com.openclassrooms.project.poseidon.services.TradeService;
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
public class TradeController
{
    private final TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "trades", tradeService.getAllTrades( ) );

        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTradeForm( Trade trade )
    {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate( @Valid Trade trade, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "trade/add";
        }

        tradeService.addNewTrade( trade );

        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer tradeId, Model model )
    {
        Trade tradeToUpdate = tradeService.findTradeById( tradeId );
        model.addAttribute( "trade", tradeToUpdate );

        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade( @PathVariable( "id" ) Integer tradeId, @Valid Trade trade, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "trade/update";
        }

        tradeService.updateTrade( tradeId, trade );

        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade( @PathVariable( "id" ) Integer tradeId )
    {
        tradeService.deleteTrade( tradeId );

        return "redirect:/trade/list";
    }
}
