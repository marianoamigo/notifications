package com.api.notifications.controllers;


import com.api.notifications.dtos.LoginRequest;
import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.services.UserService;
import com.api.notifications.utils.JWTUtil;
import com.api.notifications.utils.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) throws ErrorService {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) throws ErrorService {
        String token = userService.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}
