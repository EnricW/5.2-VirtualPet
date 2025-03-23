package s05.virtualpet.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s05.virtualpet.dto.UserDTO;
import s05.virtualpet.enums.UserRole;
import s05.virtualpet.exception.custom.InvalidCredentialsException;
import s05.virtualpet.exception.custom.UsernameAlreadyExistsException;
import s05.virtualpet.model.User;
import s05.virtualpet.repository.UserRepository;
import s05.virtualpet.security.jwt.JwtUtil;
import s05.virtualpet.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, hashedPassword, UserRole.ROLE_USER);
        user = userRepository.save(user);

        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public String authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole().name());
    }
}