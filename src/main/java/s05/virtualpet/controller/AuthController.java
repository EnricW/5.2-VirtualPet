package s05.virtualpet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.UserLoginDTO;
import s05.virtualpet.dto.UserRegisterDTO;
import s05.virtualpet.service.UserService;
import jakarta.validation.Valid;

import java.util.Collections;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO request) {
        userService.registerUser(request.username(), request.password());
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginRequest) {
        String token = userService.authenticateUser(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}