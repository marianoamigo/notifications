package com.api.notifications.client;
import dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EmailClient {

    private final RestTemplate restTemplate = new RestTemplate();


    public ResponseEntity<NotificationDTO> send(NotificationDTO notificacion) {
        String url = "http://email:8082/api/email/send";
        return restTemplate.postForEntity(url, notificacion, NotificationDTO.class);
    }
}
