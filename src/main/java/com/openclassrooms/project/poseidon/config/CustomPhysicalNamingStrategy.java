package com.openclassrooms.project.poseidon.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy
{
    @Override
    public Identifier toPhysicalCatalogName( Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context )
    {
        return convertToCamelCase( name );
    }

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
