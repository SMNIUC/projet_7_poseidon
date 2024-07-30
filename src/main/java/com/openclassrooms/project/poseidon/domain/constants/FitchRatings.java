package com.openclassrooms.project.poseidon.domain.constants;

import lombok.Getter;

@Getter
public enum FitchRatings
{
    AAA( "AAA" ),
    AAPLUS("AA+"),
    AA("AA"),
    AAMINUS("AA-"),
    APLUS("A+"),
    A("A"),
    AMINUS("A-"),
    BBBPLUS("BBB+"),
    BBB("BBB"),
    BBBMINUS("BBB-"),
    BBPLUS("BB+"),
    BB("BB"),
    BBMINUS("BB-"),
    BPLUS("B+"),
    B("B"),
    BMINUS("B-"),
    CCC("CCC"),
    CC("CC"),
    C("C"),
    RD("RD"),
    D("D");

    private final String displayValue;

    FitchRatings( String displayValue )
    {
        this.displayValue = displayValue;
    }
}
