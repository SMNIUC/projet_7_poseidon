package com.openclassrooms.project.poseidon.domain;

import com.openclassrooms.project.poseidon.config.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User
{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Please enter a username.")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Please enter a password.")
    @ValidPassword
    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "role")
    private String role;
}
