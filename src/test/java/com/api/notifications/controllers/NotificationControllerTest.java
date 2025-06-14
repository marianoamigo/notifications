package com.api.notifications.controllers;
import com.api.notifications.client.EmailClient;
import com.api.notifications.client.PushClient;
import com.api.notifications.client.SmsClient;
import com.api.notifications.models.Notification;
import com.api.notifications.models.UserModel;
import com.api.notifications.repositories.INotificationRepository;
import com.api.notifications.repositories.IUserRepository;
import com.api.notifications.utils.AuthUtilTest;
import com.api.notifications.utils.PasswordEncoder;
import dto.NotificationDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {
    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Integer userId;
    private Integer notificationId;
    @MockitoBean
    private EmailClient emailClient;
    @MockitoBean
    private SmsClient smsClient;
    @MockitoBean
    private PushClient pushClient;

    @Autowired
    private AuthUtilTest authUtilTest;
    private HttpHeaders headers;


    @BeforeEach
    public void setUpUser() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();
        UserModel user1 = userRepository.findByMail("test@mail.com").orElseGet(() -> {
            UserModel user = new UserModel();
            user.setMail("test@mail.com");
            user.setPass(passwordEncoder.encode("12345678"));
            return userRepository.save(user);
        });
        userId = user1.getId();
        headers = authUtilTest.createHeadersWithToken();
        Notification noti = new Notification();
        noti.setTitle("Titulo Test");
        noti.setBody("Cuerpo Test");
        noti.setUser(user1);
        noti.setChannel("email");

        notificationId = notificationRepository.save(noti).getId();
    }

    @Test
    public void register(){
        NotificationDTO noti = new NotificationDTO();
        noti.setTitle("Titulo Test");
        noti.setBody("Cuerpo Test");
        noti.setChannel("email");

        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        setClientResult(resultadoClient);

        //mockear la respuesta del cliente
        Mockito.when(emailClient.send(Mockito.any())).thenReturn(ResponseEntity.ok(resultadoClient));

        HttpEntity<NotificationDTO> entity = new HttpEntity<>(noti, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/notifications/register", entity, String.class);


        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Notificacion creada exitosamente", response.getBody());
    }

    @Test
    public void modify() {
        NotificationDTO noti = new NotificationDTO();
        noti.setTitle("Titulo modificado");
        noti.setBody("Cuerpo modificado");
        noti.setChannel("Sms");

        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        setClientResult(resultadoClient);

        //mockear la respuesta del cliente
        Mockito.when(smsClient.send(Mockito.any())).thenReturn(ResponseEntity.ok(resultadoClient));

        HttpEntity<NotificationDTO> entity = new HttpEntity<>(noti,headers);
        ResponseEntity<String> response = restTemplate.exchange("/api/notifications/modify/"+ notificationId, HttpMethod.PUT, entity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Notificacion modificada exitosamente", response.getBody());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void getNotification() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Notification> response = restTemplate.exchange(
                "/api/notifications/notification/" + notificationId,
                HttpMethod.GET,
                entity,
                Notification.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void getNotificationsByUser() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Notification>> response = restTemplate.exchange(
                "/api/notifications/all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Notification>>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
    }



    @Test
    public void delete() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/notifications/delete/"+ notificationId,
                HttpMethod.DELETE,
                entity,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Notificación eliminada", response.getBody());
        Assertions.assertNotNull(response.getBody());
    }

    private void setClientResult(NotificationDTO resultadoClient) {
        resultadoClient.setTitle("Titulo"); //no deberian cambiar en el microservicio
        resultadoClient.setBody("Cuerpo");//
        resultadoClient.setChannel("email");//
        resultadoClient.setRecipient("ejemplo@mail.com");
        resultadoClient.setNumSend(100);
        resultadoClient.setSendDate(LocalDateTime.now());
        resultadoClient.setTokenDevice("token_fake_recibido");
    }
}
