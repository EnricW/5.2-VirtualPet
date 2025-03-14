package s05.virtualpet.dto;

import s05.virtualpet.enums.UserRole;

public record UserDTO(Long id, String username, UserRole role) {}