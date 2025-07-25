package com.api.notifications.repositories;

import com.api.notifications.client.EmailClient;
import com.api.notifications.errors.ErrorClient;
import dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;

@Repository
public class EmailRepository {

    private EmailClient email;

    public EmailRepository(EmailClient email) {
        this.email = email;
    }

    public NotificationDTO send(NotificationDTO notificationDTO) {
        try {
            ResponseEntity<NotificationDTO> response = email.send(notificationDTO);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new ErrorClient("Fallo al enviar email: codigo HTTP " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new ErrorClient("Error al contactar al servicio de email " + e);
        }
    }
}
