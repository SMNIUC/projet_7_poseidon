package com.openclassrooms.project.poseidon.domain.dto;

import com.openclassrooms.project.poseidon.config.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDTO
{
    private int id;

    @NotBlank(message = "Please enter a username.")
    private String username;

    @NotBlank(message = "Please enter a password.")
    @ValidPassword
    private String password;

    private String fullname;

    private String role;
}
