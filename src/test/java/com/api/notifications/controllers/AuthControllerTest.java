package com.api.notifications.controllers;

import com.api.notifications.dtos.LoginRequest;
import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.models.Usuario;
import com.api.notifications.repositories.IUserRepository;
import com.api.notifications.utils.PasswordEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository usuarioRepository;


    @BeforeEach
    public void setUpUser() {
        usuarioRepository.findByMail("test@mail.com").orElseGet(() -> {
            Usuario user = new Usuario();
            user.setMail("test@mail.com");
            user.setPass(passwordEncoder.encode("12345678"));
            return usuarioRepository.save(user);
        });
    }

    @Test
    public void register(){
        RegisterRequest request = new RegisterRequest();
        request.setMail("nuevo@mail.com");
        request.setPass("12345678");
        request.setSegundaPass("12345678");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", request, String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Usuario registrado con éxito", response.getBody());
    }

    @Test
    public void login(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMail("test@mail.com");
        loginRequest.setPass("12345678");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login",loginRequest, String.class);
        String token = response.getBody();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }
}
