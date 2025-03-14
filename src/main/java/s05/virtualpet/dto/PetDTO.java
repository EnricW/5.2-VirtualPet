package s05.virtualpet.dto;

import s05.virtualpet.enums.Luck;
import s05.virtualpet.enums.PetType;

public record PetDTO(Long id, String name, PetType type, Luck luck, int chips) {}
