package com.api.notifications.controllers;
import com.api.notifications.client.EmailClient;
import com.api.notifications.client.PushClient;
import com.api.notifications.client.SmsClient;
import com.api.notifications.models.Notification;
import com.api.notifications.models.Usuario;
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
    private INotificationRepository notificacionRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IUserRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private Integer usuarioId;
    private Integer notificacionId;
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
        Usuario usuario1 = usuarioRepository.findByMail("test@mail.com").orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setMail("test@mail.com");
            usuario.setPass(passwordEncoder.encode("12345678"));
            return usuarioRepository.save(usuario);
        });
        usuarioId = usuario1.getId();
        headers = authUtilTest.createHeadersWithToken();
        notificacionId = 1;
        Notification notification = notificacionRepository.findById(notificacionId).orElseGet(() ->{
            Notification noti = new Notification();
            noti.setTitle("Titulo Test");
            noti.setBody("Cuerpo Test");
            noti.setUser(usuario1);
            noti.setChannel("email");
            return notificacionRepository.save(noti);
        });
    }

    @Test
    public void registrar(){
        NotificationDTO noti = new NotificationDTO();
        noti.setTitle("Titulo Test");
        noti.setBody("Cuerpo Test");
        noti.setChannel("email");

        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        setearResultadoClient(resultadoClient);

        //mockear la respuesta del cliente
        Mockito.when(emailClient.send(Mockito.any())).thenReturn(ResponseEntity.ok(resultadoClient));

        HttpEntity<NotificationDTO> entity = new HttpEntity<>(noti, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/notificaciones/registrar", entity, String.class);


        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("Notificacion creada exitosamente", response.getBody());
    }

    @Test
    public void modificar() {
        NotificationDTO noti = new NotificationDTO();
        noti.setTitle("Titulo modificado");
        noti.setBody("Cuerpo modificado");
        noti.setChannel("Sms");

        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        setearResultadoClient(resultadoClient);

        //mockear la respuesta del cliente
        Mockito.when(smsClient.send(Mockito.any())).thenReturn(ResponseEntity.ok(resultadoClient));

        HttpEntity<NotificationDTO> entity = new HttpEntity<>(noti,headers);
        ResponseEntity<String> response = restTemplate.exchange("/api/notificaciones/modificar/"+notificacionId, HttpMethod.PUT, entity, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Notificacion modificada exitosamente", response.getBody());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void verNotificacion() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Notification> response = restTemplate.exchange(
                "/api/notificaciones/notificacion/" + notificacionId,
                HttpMethod.GET,
                entity,
                Notification.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void verTodasPorUsuario() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List<Notification>> response = restTemplate.exchange(
                "/api/notificaciones/todas",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Notification>>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
    }



    @Test
    public void eliminar() {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/notificaciones/eliminar/"+notificacionId,
                HttpMethod.DELETE,
                entity,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Notificación eliminada", response.getBody());
        Assertions.assertNotNull(response.getBody());
    }

    private void setearResultadoClient(NotificationDTO resultadoClient) {
        resultadoClient.setTitle("Titulo"); //no deberian cambiar en el microservicio
        resultadoClient.setBody("Cuerpo");//
        resultadoClient.setChannel("email");//
        resultadoClient.setRecipient("ejemplo@mail.com");
        resultadoClient.setNumSend(100);
        resultadoClient.setSendDate(LocalDateTime.now());
        resultadoClient.setTokenDevice("token_fake_recibido");
    }
}
