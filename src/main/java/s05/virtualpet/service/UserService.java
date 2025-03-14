package s05.virtualpet.service;

import s05.virtualpet.model.User;

public interface UserService {
    User registerUser(String username, String password);
    User findByUsername(String username);
}
