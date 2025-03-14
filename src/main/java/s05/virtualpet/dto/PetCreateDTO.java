package s05.virtualpet.dto;

import jakarta.validation.constraints.NotBlank;
import s05.virtualpet.enums.PetType;

public record PetCreateDTO(
        @NotBlank String name,
        PetType type
) {}
