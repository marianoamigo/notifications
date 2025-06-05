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
@RequestMapping("/api/usuarios")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<Usuario> getUser(@PathVariable Integer id, @RequestHeader(value="Authorization") String token) throws ErrorService {
        Usuario usuario = userService.verUsuarioPorId(id, token);
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> modificarUsuario(@PathVariable Integer id, @RequestBody RegisterRequest request, @RequestHeader(value="Authorization") String token) throws ErrorService {
        userService.modificar(id, request, token);
        return ResponseEntity.ok("Usuario modificado con éxito");
    }

}