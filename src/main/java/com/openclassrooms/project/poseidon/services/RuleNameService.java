package com.openclassrooms.project.poseidon.services;

import com.openclassrooms.project.poseidon.domain.RuleName;
import com.openclassrooms.project.poseidon.repositories.RuleNameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RuleNameService
{
    private final RuleNameRepository ruleNameRepository;

    public List<RuleName> getAllRulenames( )
    {
        List<RuleName> allRulenamesList = new ArrayList<>( );
        ruleNameRepository.findAll( ).forEach( allRulenamesList::add );
        return allRulenamesList;
    }

    public RuleName findRulenameById( Integer ruleId )
    {
        return ruleNameRepository.getRuleNameById( ruleId );
    }

    @Transactional
    public void addNewRulename( RuleName ruleName )
    {
        ruleNameRepository.save( ruleName );
    }

    @Transactional
    public void updateRulename( RuleName ruleName, Integer ruleId )
    {
        RuleName ruleNameToUpdate = findRulenameById( ruleId );
        ruleNameToUpdate.setName( ruleName.getName( ) );
        ruleNameToUpdate.setDescription( ruleName.getDescription( ) );
        ruleNameToUpdate.setJson( ruleName.getJson( ) );
        ruleNameToUpdate.setTemplate( ruleName.getTemplate( ) );
        ruleNameToUpdate.setSqlPart( ruleName.getSqlPart( ) );
        ruleNameToUpdate.setSqlStr( ruleNameToUpdate.getSqlStr( ) );
        ruleNameRepository.save( ruleNameToUpdate );
    }

    @Transactional
    public void deleteRulename( Integer ruleId )
    {
        RuleName rulenameToDelete = findRulenameById( ruleId );
        ruleNameRepository.delete( rulenameToDelete );
    }
}
