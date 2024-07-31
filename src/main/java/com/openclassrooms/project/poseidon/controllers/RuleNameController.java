package com.openclassrooms.project.poseidon.controllers;

import com.openclassrooms.project.poseidon.domain.RuleName;
import com.openclassrooms.project.poseidon.services.RuleNameService;
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
public class RuleNameController
{
    private final RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home( HttpServletRequest request, Model model )
    {
        model.addAttribute( "httpServletRequest", request );
        model.addAttribute( "ruleNames", ruleNameService.getAllRulenames( ) );

        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm( RuleName ruleName )
    {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate( @Valid RuleName ruleName, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "ruleName/add";
        }

        ruleNameService.addNewRulename( ruleName );

        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm( @PathVariable( "id" ) Integer ruleId, Model model )
    {
        RuleName ruleNameToUpdate = ruleNameService.findRulenameById( ruleId );
        model.addAttribute( "ruleName", ruleNameToUpdate );

        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName( @PathVariable( "id" ) Integer ruleId, @Valid RuleName ruleName, BindingResult result )
    {
        if ( result.hasErrors( ) )
        {
            return "ruleName/update";
        }

        ruleNameService.updateRulename( ruleName, ruleId );

        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName( @PathVariable( "id" ) Integer ruleId )
    {
        ruleNameService.deleteRulename( ruleId );

        return "redirect:/ruleName/list";
    }
}
