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
        return ruleNameRepository.findById( ruleId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid rule Id:" + ruleId ) );
    }

    @Transactional
    public void addNewRulename( RuleName ruleName )
    {
        ruleNameRepository.save( ruleName );
    }

    @Transactional
    public void updateRulename( RuleName ruleName, Integer ruleId )
    {
        ruleName.setId( ruleId );
        ruleNameRepository.save( ruleName );
    }

    @Transactional
    public void deleteRulename( Integer ruleId )
    {
        RuleName rulenameToDelete = findRulenameById( ruleId );
        ruleNameRepository.delete( rulenameToDelete );
    }
}
