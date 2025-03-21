package s05.virtualpet.service;

import s05.virtualpet.dto.UserDTO;
import s05.virtualpet.model.User;

import java.util.Optional;

public interface UserService {
    UserDTO registerUser(String username, String password);
    Optional<UserDTO> findByUsername(String username);
    String authenticateUser(String username, String password);
}
