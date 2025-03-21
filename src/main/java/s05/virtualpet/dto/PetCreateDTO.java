package s05.virtualpet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import s05.virtualpet.enums.PetType;

public record PetCreateDTO(
        @NotBlank
        @Size(min = 3, max = 15, message = "Pet name must be between 3 and 15 characters")
        String name,

        @NotNull(message = "Pet type must be selected")
        PetType type
) {}
