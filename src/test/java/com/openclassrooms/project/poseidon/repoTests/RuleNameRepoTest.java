package com.openclassrooms.project.poseidon.repoTests;

import com.openclassrooms.project.poseidon.domain.RuleName;
import com.openclassrooms.project.poseidon.repositories.RuleNameRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleNameRepoTest
{
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Test
    public void ruleTest( )
    {
        RuleName rule = new RuleName( );
        rule.setName( "Rule Name" );
        rule.setDescription( "Description" );
        rule.setJson( "Json" );
        rule.setTemplate( "Template" );
        rule.setSqlStr( "SQL" );
        rule.setSqlPart( "SQL Part" );

        // Save
        rule = ruleNameRepository.save(rule);
        Assert.assertNotNull(rule.getId());
        Assert.assertEquals( "Rule Name", rule.getName( ) );

        // Update
        rule.setName("Rule Name Update");
        rule = ruleNameRepository.save(rule);
        Assert.assertEquals( "Rule Name Update", rule.getName( ) );

        // Find
        List<RuleName> listResult = ( List<RuleName> ) ruleNameRepository.findAll();
        Assert.assertFalse( listResult.isEmpty( ) );

        // Delete
        Integer id = rule.getId();
        ruleNameRepository.delete(rule);
        Optional<RuleName> ruleList = ruleNameRepository.findById(id);
        Assert.assertFalse(ruleList.isPresent());
    }
}
