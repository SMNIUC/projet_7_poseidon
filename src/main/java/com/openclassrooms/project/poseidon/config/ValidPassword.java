package com.openclassrooms.project.poseidon.config;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for validating that a password meets certain security criteria.
 *
 * <p>
 * The {@code ValidPassword} annotation can be applied to fields, types, or other annotations to enforce
 * password validation rules defined in the {@link PasswordConstraintValidator} class.
 * </p>
 *
 * <p>
 * This annotation is processed by the {@link PasswordConstraintValidator}, which checks that the annotated
 * password field meets various conditions such as minimum length, presence of uppercase letters, digits,
 * special characters, and absence from a list of common passwords.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @ValidPassword
 * private String password;
 * }
 * </pre>
 * </p>
 *
 * @see PasswordConstraintValidator
 */
@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword
{
    /**
     * The default error message that will be returned when the password validation fails.
     *
     * @return the error message
     */
    String message() default "Invalid Password";

    /**
     * Allows the specification of validation groups, to which this constraint belongs.
     *
     * <p>
     * This must default to an empty array of {@code Class} objects.
     * </p>
     *
     * @return an array of classes representing the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients of the Jakarta Bean Validation API to assign custom payload objects
     * to a constraint.
     *
     * <p>
     * This attribute is not used by the validation engine itself. It is intended for clients
     * who wish to provide additional information about the validation failure.
     * </p>
     *
     * @return an array of classes that extend {@link Payload}
     */
    Class<? extends Payload>[] payload() default {};
}
