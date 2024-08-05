package com.openclassrooms.project.poseidon.serviceTests;

import com.openclassrooms.project.poseidon.domain.RuleName;
import com.openclassrooms.project.poseidon.repositories.RuleNameRepository;
import com.openclassrooms.project.poseidon.services.RuleNameService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleNameServiceTest
{
    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameService ruleNameServiceUnderTest;

    private static RuleName rule;

    @BeforeAll
    static void setUp( )
    {
        rule = new RuleName( );
        rule.setId( 1 );
        rule.setName( "Test Name" );
        rule.setDescription( "Test Description" );
        rule.setJson( "Test Json" );
        rule.setTemplate( "Test Template" );
        rule.setSqlStr( "Test Sql String" );
        rule.setSqlPart( "Test Sql Part" );
    }

    @Test
    void getAllRulenames( )
    {
        // GIVEN
        when( ruleNameRepository.findAll( ) ).thenReturn( List.of( rule ) );

        // WHEN
        List<RuleName> ruleNameList = ruleNameServiceUnderTest.getAllRulenames( );

        // THEN
        assertThat( ruleNameList.size( ) ).isEqualTo( 1 );
        assertThat( ruleNameList.get( 0 ) ).isEqualTo( rule );
    }

    @Test
    void findRulenameById( )
    {
        // GIVEN
        when( ruleNameRepository.findById( anyInt( ) ) ).thenReturn( Optional.of( rule ) );

        // WHEN
        RuleName ruleName = ruleNameServiceUnderTest.findRulenameById( 1 );

        // THEN
        assertThat( ruleName ).isEqualTo( rule );
    }

    @Test
    void findRuleNameByIdError( )
    {
        // GIVEN
        when( ruleNameRepository.findById( anyInt( ) ) ).thenReturn( Optional.empty( ) );
        String expectedMessage = "Invalid rule Id:" + 1;

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows( IllegalArgumentException.class,
                ( ) -> ruleNameServiceUnderTest.findRulenameById( 1 ) );
        assertThat( expectedMessage ).isEqualTo( exception.getMessage( ) );
    }

    @Test
    void addNewRulename( )
    {
        // WHEN
        ruleNameServiceUnderTest.addNewRulename( rule );

        // THEN
        verify( ruleNameRepository ).save( rule );
    }

    @Test
    void updateRulename( )
    {
        // GIVEN
        RuleName updatedRule = new RuleName( );
        updatedRule.setName( "Updated Name" );

        // WHEN
        ruleNameServiceUnderTest.updateRulename( updatedRule, 1 );

        // THEN
        verify( ruleNameRepository ).save( updatedRule );
        assertThat( updatedRule.getId( ) ).isEqualTo( 1 );
    }

    @Test
    void deleteRulename( )
    {
        // GIVEN
        when( ruleNameRepository.findById( 1 ) ).thenReturn( Optional.of( rule ) );

        // WHEN
        ruleNameServiceUnderTest.deleteRulename( 1 );

        // THEN
        verify( ruleNameRepository ).delete( rule );
    }
}
