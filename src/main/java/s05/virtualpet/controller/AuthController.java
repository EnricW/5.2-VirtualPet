package s05.virtualpet.controller;

import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.UserDTO;
import s05.virtualpet.dto.UserRegisterDTO;
import s05.virtualpet.model.User;
import s05.virtualpet.service.UserService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserRegisterDTO request) {
        User user = userService.registerUser(request.username(), request.password());
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return new UserDTO(user.getId(), user.getUsername(), user.getRole());
    }
}