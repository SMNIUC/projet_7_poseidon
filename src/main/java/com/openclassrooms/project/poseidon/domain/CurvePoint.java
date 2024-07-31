package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "curvepoint")
public class CurvePoint
{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "must not be null.")
    //TODO -> not showing, Failed to convert property value of type java.lang.String to required type java.lang.Integer
    // for property curveId; For input string: "jgkmgu" message instead
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "must contain only digits.")
    @Column(name = "CurveId")
    private Integer curveId;

    @Column(name = "asOfDate")
    private Timestamp asOfDate;

    @NotNull(message = "must not be null.")
//    @Digits(integer = 10, fraction = 2, message = "must be a numeric value with up to 10 digits and 2 decimal places.")
    @Column(name = "term")
    private Double term;

    @NotNull(message = "must not be null.")
//    @Digits(integer = 10, fraction = 2, message = "must be a numeric value with up to 10 digits and 2 decimal places.")
    @Column(name = "value")
    private Double value;

    @Column(name = "creationDate")
    private Timestamp creationDate;
}
