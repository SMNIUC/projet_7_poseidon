package com.openclassrooms.project.poseidon.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * CustomPhysicalNamingStrategy is a custom implementation of Hibernate's {@link PhysicalNamingStrategy} interface.
 * This strategy converts database object names (e.g., table names, column names) from snake_case to camelCase.
 *
 * <p>
 * This strategy can be useful in environments where the database uses snake_case naming conventions,
 * but the application prefers camelCase for naming consistency.
 * </p>
 */
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy
{
    /**
     * Converts the catalog name from snake_case to camelCase.
     *
     * @param name    the catalog name in snake_case format, may be {@code null}
     * @param context the JDBC environment context, providing access to database metadata
     * @return the catalog name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    @Override
    public Identifier toPhysicalCatalogName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    /**
     * Converts the schema name from snake_case to camelCase.
     *
     * @param name    the schema name in snake_case format, may be {@code null}
     * @param context the JDBC environment context, providing access to database metadata
     * @return the schema name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    @Override
    public Identifier toPhysicalSchemaName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    /**
     * Converts the table name from snake_case to camelCase.
     *
     * @param name    the table name in snake_case format, may be {@code null}
     * @param context the JDBC environment context, providing access to database metadata
     * @return the table name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    @Override
    public Identifier toPhysicalTableName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    /**
     * Converts the sequence name from snake_case to camelCase.
     *
     * @param name    the sequence name in snake_case format, may be {@code null}
     * @param context the JDBC environment context, providing access to database metadata
     * @return the sequence name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    @Override
    public Identifier toPhysicalSequenceName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    /**
     * Converts the column name from snake_case to camelCase.
     *
     * @param name    the column name in snake_case format, may be {@code null}
     * @param context the JDBC environment context, providing access to database metadata
     * @return the column name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    @Override
    public Identifier toPhysicalColumnName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    /**
     * Converts a given identifier name from snake_case to camelCase.
     *
     * <p>This method handles the conversion of underscores to camel case.
     * For example, the name "example_table" would be converted to "exampleTable".</p>
     *
     * @param name the identifier name in snake_case format, may be {@code null}
     * @return the identifier name converted to camelCase, or {@code null} if the input name was {@code null}
     */
    private Identifier convertToCamelCase( Identifier name )
    {
        if ( name == null )
        {
            return null;
        }
        String snakeCase = name.getText( );
        StringBuilder camelCase = new StringBuilder( );

        boolean toUpperCase = false;
        for ( char c : snakeCase.toCharArray( ) )
        {
            if ( c == '_' )
            {
                toUpperCase = true;
            } else
            {
                if ( toUpperCase )
                {
                    camelCase.append( Character.toUpperCase( c ) );
                    toUpperCase = false;
                } else
                {
                    camelCase.append( c );
                }
            }
        }

        return Identifier.toIdentifier( camelCase.toString( ) );
    }
}
