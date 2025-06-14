package com.api.notifications.client;
import dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PushClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<NotificationDTO> send(NotificationDTO notificacion) {
        String url = "http://localhost:8084/api/push/send";
        return restTemplate.postForEntity(url,notificacion, NotificationDTO.class);
    }
}
