package com.openclassrooms.project.poseidon.domain.constants;

import lombok.Getter;

@Getter
public enum MoodysRatings
{
    AAA( "Aaa" ),
    AA1("Aa1"),
    AA2("Aa2"),
    AA3("Aa3"),
    A1("A1"),
    A2("A2"),
    A3("A3"),
    BAA1("Baa1"),
    BAA2("Baa2"),
    BAA3("Baa3"),
    BA1("Ba1"),
    BA2("Ba2"),
    BA3("Ba3"),
    B1("B1"),
    B2("B2"),
    B3("B3"),
    CAA1("Caa1"),
    CAA2("Caa2"),
    CAA3("Caa3"),
    CA("Ca"),
    C("C");

    private final String displayValue;

    MoodysRatings( String displayValue )
    {
        this.displayValue = displayValue;
    }
}
