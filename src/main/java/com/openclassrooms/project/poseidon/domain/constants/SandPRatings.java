package com.openclassrooms.project.poseidon.domain.constants;

import lombok.Getter;

@Getter
public enum SandPRatings
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
    CCCPLUS("CCC+"),
    CCC("CCC"),
    CCCMINUS("CCC-"),
    CC("CC"),
    C("C"),
    SD("SD"),
    D("D");

    private final String displayValue;

    SandPRatings( String displayValue )
    {
        this.displayValue = displayValue;
    }
}
