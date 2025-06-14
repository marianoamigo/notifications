package com.api.notifications.client;
import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SmsClient {

    @Value("${client.sms.url}")
    private String url;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<NotificationDTO> send(NotificationDTO notificacion) {
        return restTemplate.postForEntity(url,notificacion, NotificationDTO.class);
    }
}

