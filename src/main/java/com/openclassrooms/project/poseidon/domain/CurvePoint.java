package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
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

    @Column(name = "CurveId")
    private Integer curveId;

    @Column(name = "asOfDate")
    private Timestamp asOfDate;

    @Column(name = "term")
    private Double term;

    @Column(name = "value")
    private Double value;

    @Column(name = "creationDate")
    private Timestamp creationDate;
}
