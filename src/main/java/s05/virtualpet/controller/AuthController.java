package s05.virtualpet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s05.virtualpet.dto.UserLoginDTO;
import s05.virtualpet.dto.UserRegisterDTO;
import s05.virtualpet.service.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with a username and password."
    )
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDTO request) {
        userService.registerUser(request.username(), request.password());
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with username and password, returning a JWT token upon success."
    )
    @ApiResponse(responseCode = "200", description = "Login successful, token returned")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginRequest) {
        String token = userService.authenticateUser(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}