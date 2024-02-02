package com.fiap.streamingvideo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserDTO(
    String id,
    @NotBlank @Length(max = 100) String name,
    @NotBlank String cpf,
    @Email @NotBlank String email
) {
}
