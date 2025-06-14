package com.api.notifications.controllers;

import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.models.UserModel;
import com.api.notifications.repositories.IUserRepository;
import com.api.notifications.utils.AuthUtilTest;
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
public class UserControllerTest {
    @Autowired
    private IUserRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthUtilTest authUtilTest;
    @Autowired
    private TestRestTemplate restTemplate;
    private Integer usuarioId;
    private HttpHeaders headers;


    @BeforeEach
    public void setUpUser() {
        UserModel userModel1 = usuarioRepository.findByMail("test@mail.com").orElseGet(() -> {
            UserModel userModel = new UserModel();
            userModel.setMail("test@mail.com");
            userModel.setPass(passwordEncoder.encode("12345678"));
            return usuarioRepository.save(userModel);
        });
        usuarioId = userModel1.getId();
        headers = authUtilTest.createHeadersWithToken();
    }

    @Test
    public void verUsuario() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<UserModel> response = restTemplate.exchange(
                "/api/users/user/" + usuarioId,
                HttpMethod.GET,
                entity,
                UserModel.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("test@mail.com",response.getBody().getMail());
    }


    @Test
    public void modificar() {
        RegisterRequest request = new RegisterRequest();
        request.setMail("modificado@mail.com");
        request.setPass("12345678");
        request.setSegundaPass("12345678");
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users/modify/"+usuarioId,
                HttpMethod.PUT,
                entity,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Usuario modificado con éxito", response.getBody());
        Assertions.assertNotNull(response.getBody());
    }

}
