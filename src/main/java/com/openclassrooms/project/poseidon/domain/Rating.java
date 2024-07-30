package com.openclassrooms.project.poseidon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "rating")
public class Rating
{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Please select a rating.")
    @Column(name = "moodysRating")
    private String moodysRating;

    @NotBlank(message = "Please select a rating.")
    @Column(name = "sandPRating")
    private String sandPRating;

    @NotBlank(message = "Please select a rating.")
    @Column(name = "fitchRating")
    private String fitchRating;

    @NotNull(message = "Please enter the order number.")
    @Column(name = "orderNumber")
    private Integer orderNumber;
}
