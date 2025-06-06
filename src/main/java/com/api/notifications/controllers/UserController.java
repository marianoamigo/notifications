package com.api.notifications.controllers;

import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.services.UserService;
import com.api.notifications.utils.JWTUtil;
import com.api.notifications.utils.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.notifications.models.Usuario;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/{id}")
    public ResponseEntity<Usuario> getUser(@PathVariable Integer id, @RequestHeader(value="Authorization") String token) throws ErrorService {
        Usuario user = userService.getUserById(id, token);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyUser(@PathVariable Integer id, @RequestBody RegisterRequest request, @RequestHeader(value="Authorization") String token) throws ErrorService {
        userService.modify(id, request, token);
        return ResponseEntity.ok("Usuario modificado con éxito");
    }

}