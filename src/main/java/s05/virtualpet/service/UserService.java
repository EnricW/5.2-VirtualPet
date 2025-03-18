package s05.virtualpet.service;

import s05.virtualpet.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(String username, String password);
    Optional<User> findByUsername(String username);
    String authenticateUser(String username, String password);
}
