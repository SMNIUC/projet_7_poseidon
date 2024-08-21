package com.openclassrooms.project.poseidon.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * PasswordConstraintValidator is a custom validator that enforces password strength and validity rules.
 * This class implements the {@link ConstraintValidator} interface for the {@link ValidPassword} annotation.
 *
 * <p>
 * The validator checks passwords against a set of rules, including length, character types,
 * whitespace, and a dictionary of common passwords. If a password violates any of these rules,
 * it is considered invalid.
 * </p>
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String>
{
    private DictionaryRule dictionaryRule;

    /**
     * Initializes the validator, loading a list of common (invalid) passwords from a file.
     *
     * <p>
     * This method reads a file containing a list of common passwords, which are considered invalid.
     * The file is expected to be located in the classpath as "invalid-password-list.txt".
     * </p>
     *
     * @param constraintAnnotation the annotation instance for a given constraint declaration
     * @throws RuntimeException if the word list cannot be loaded
     */
    @Override
    public void initialize( ValidPassword constraintAnnotation )
    {
        try {
            String invalidPasswordList = Objects.requireNonNull( this.getClass( ).getResource( "/invalid-password-list.txt" ) ).getFile( );
            dictionaryRule = new DictionaryRule(
                    new WordListDictionary( WordLists.createFromReader(
                            // Reader around the word list file
                            new FileReader[] {
                                    new FileReader( invalidPasswordList )
                            },
                            // True for case sensitivity, false otherwise
                            false,
                            // Dictionaries must be sorted
                            new ArraysSort( )
                    )));
        } catch ( IOException e )
        {
            throw new RuntimeException( "could not load word list", e ) ;
        }
    }

    /**
     * Validates the password against a set of security rules.
     *
     * <p>
     * The rules include:
     * <ul>
     *   <li>Minimum length of 8 characters</li>
     *   <li>At least one upper-case character</li>
     *   <li>At least one lower-case character</li>
     *   <li>At least one digit</li>
     *   <li>At least one special character</li>
     *   <li>No whitespace</li>
     *   <li>Must not be a common password</li>
     * </ul>
     * If the password violates any of these rules, the method returns {@code false} and provides
     * feedback messages to the validation context.
     * </p>
     *
     * @param password the password to validate
     * @param context  the context in which the constraint is evaluated
     * @return {@code true} if the password is valid, {@code false} otherwise
     */
    @Override
    public boolean isValid( String password, ConstraintValidatorContext context )
    {
        PasswordValidator validator = new PasswordValidator( Arrays.asList(

                // at least 8 characters
                new LengthRule( 8, 30 ),

                // at least one upper-case character
                new CharacterRule( EnglishCharacterData.UpperCase, 1 ),

                // at least one lower-case character
                new CharacterRule( EnglishCharacterData.LowerCase, 1 ),

                // at least one digit character
                new CharacterRule( EnglishCharacterData.Digit, 1 ),

                // at least one symbol (special character)
                new CharacterRule( EnglishCharacterData.Special, 1 ),

                // no whitespace
                new WhitespaceRule( ),

                // no common passwords
                dictionaryRule
        ));

        RuleResult result = validator.validate( new PasswordData( password ) );

        if ( result.isValid( ) )
        {
            return true;
        }

        List<String> messages = validator.getMessages( result );
        String messageTemplate = String.join( ",", messages );
        context.buildConstraintViolationWithTemplate( messageTemplate )
                .addConstraintViolation( )
                .disableDefaultConstraintViolation( );

        return false;
    }
}
