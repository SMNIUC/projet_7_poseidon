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

    /**
     * Gets all the available rules from the database
     *
     * @return a list of rules
     */
    public List<RuleName> getAllRulenames( )
    {
        List<RuleName> allRulenamesList = new ArrayList<>( );
        ruleNameRepository.findAll( ).forEach( allRulenamesList::add );

        return allRulenamesList;
    }

    /**
     * Finds a rule in the database from its id
     *
     * @param ruleId the id of the rule to be found
     * @return a rule
     */
    public RuleName findRulenameById( Integer ruleId )
    {
        return ruleNameRepository.findById( ruleId )
                .orElseThrow( ( ) -> new IllegalArgumentException( "Invalid rule Id:" + ruleId ) );
    }

    /**
     * Creates and saves a new rule in the database
     *
     * @param ruleName the rule to be saved
     */
    @Transactional
    public void addNewRulename( RuleName ruleName )
    {
        ruleNameRepository.save( ruleName );
    }

    /**
     * Updates a rule in the database with new info
     *
     * @param ruleId the id of the ruleName object to be updated
     * @param ruleName a ruleName object that holds the new rule information
     */
    @Transactional
    public void updateRulename( RuleName ruleName, Integer ruleId )
    {
        ruleName.setId( ruleId );
        ruleNameRepository.save( ruleName );
    }

    /**
     * Deletes a ruleName object in the database, based on its id
     *
     * @param ruleId the id of the ruleName object to be deleted
     */
    @Transactional
    public void deleteRulename( Integer ruleId )
    {
        RuleName rulenameToDelete = findRulenameById( ruleId );
        ruleNameRepository.delete( rulenameToDelete );
    }
}
