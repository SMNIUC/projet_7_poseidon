package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    @Positive(message = "must be a positive number.")
    @Column(name = "CurveId")
    private Integer curveId;

    @Column(name = "asOfDate")
    private Timestamp asOfDate;

    @NotNull(message = "must not be null.")
    @PositiveOrZero(message = "must be a positive number or zero.")
    @Column(name = "term")
    private Double term;

    @NotNull(message = "must not be null.")
    @Positive(message = "must be a positive number.")
    @Column(name = "value")
    private Double value;

    @Column(name = "creationDate")
    private Timestamp creationDate;
}
