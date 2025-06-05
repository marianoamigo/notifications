package com.api.notifications.repositories;

import com.api.notifications.client.PushClient;
import com.api.notifications.errors.ErrorClient;
import dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;

@Repository
public class PushRepository {
    private PushClient push;

    public PushRepository(PushClient push) { this.push = push;}

    public NotificationDTO send (NotificationDTO notificationDTO) {
        try {
            ResponseEntity<NotificationDTO> response = push.send(notificationDTO);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
                return response.getBody();
            } else {
                throw new ErrorClient("Fallo al enviar push: codigo HTTP "+ response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new ErrorClient("Error al contactar al servicio de email " + e);
        }
    }

}
