package s05.virtualpet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank @Size(min = 4, max = 20) String username,
        @NotBlank @Size(min = 6, max = 100) String password
) {}