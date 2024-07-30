package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "rulename")
public class RuleName
{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Please enter a rule name.")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Please enter a rule description.")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Please enter a rule json.")
    @Column(name = "json")
    private String json;

    @NotBlank(message = "Please enter a rule template.")
    @Column(name = "template")
    private String template;

    @NotBlank(message = "Please enter a rule SQL.")
    @Column(name = "sqlStr")
    private String sqlStr;

    @NotBlank(message = "Please enter a rule SQL Part.")
    @Column(name = "sqlPart")
    private String sqlPart;
}
