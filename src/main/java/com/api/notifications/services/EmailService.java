package com.api.notifications.services;

import com.api.notifications.errors.ErrorClient;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.repositories.EmailRepository;
import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmailService implements Canal {

    private final EmailRepository emailRepository;

    @Autowired
    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }
    @Override
    public String getName(){
        return "EMAIL";
    }
    @Override
    public void send(NotificationDTO notificationDTO) throws ErrorService {
        try {
            NotificationDTO result = emailRepository.send(notificationDTO);
            notificationDTO.setNumSend(result.getNumSend());
            notificationDTO.setSendDate(result.getSendDate());
        } catch (ErrorClient ec) {
            throw new ErrorService("No se pudo enviar notificacion por mail", ec);
        }
    }

}

