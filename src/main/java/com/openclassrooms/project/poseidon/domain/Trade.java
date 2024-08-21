package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "trade")
public class Trade
{
    @Id
    @Column(name = "TradeId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tradeId;

    @NotBlank(message = "Please enter an account number.")
    @Column(name = "account")
    private String account;

    @NotBlank(message = "Please enter a trade type.")
    @Column(name = "type")
    private String type;

    @NotNull(message = "must be not null.")
    @Positive(message = "must be a positive number.")
    @Column(name = "buyQuantity")
    private Double buyQuantity;

    @Column(name = "sellQuantity")
    private Double sellQuantity;

    @Column(name = "buyPrice")
    private Double buyPrice;

    @Column(name = "sellPrice")
    private Double sellPrice;

    @Column(name = "benchmark")
    private String benchmark;

    @Column(name = "tradeDate")
    private Timestamp tradeDate;

    @Column(name = "security")
    private String security;

    @Column(name = "status")
    private String status;

    @Column(name = "trader")
    private String trader;

    @Column(name = "book")
    private String book;

    @Column(name = "creationName")
    private String creationName;

    @Column(name = "creationDate")
    private Timestamp creationDate;

    @Column(name = "revisionName")
    private String revisionName;

    @Column(name = "revisionDate")
    private Timestamp revisionDate;

    @Column(name = "dealName")
    private String dealName;

    @Column(name = "dealType")
    private String dealType;

    @Column(name = "sourceListId")
    private String sourceListId;

    @Column(name = "side")
    private String side;
}
