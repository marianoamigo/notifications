package com.api.notifications.utils;

import com.api.notifications.dtos.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component
public class AuthUtilTest {
    @Autowired
    private TestRestTemplate restTemplate;

    public String loginAndGetToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMail("test@mail.com");
        loginRequest.setPass("12345678");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Login fallido en AuthUtilTest");
        }
        return response.getBody();
    }

    public HttpHeaders createHeadersWithToken() {
        String token = loginAndGetToken();
        HttpHeaders headers  = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
