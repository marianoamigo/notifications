package com.api.notifications.services;


import com.api.notifications.errors.ErrorClient;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.repositories.SmsRepository;
import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmsService implements Canal {
    private final SmsRepository smsRepository;

    @Autowired
    public SmsService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    @Override
    public String getName(){
        return "SMS";
    }

    @Override
    public void send(NotificationDTO notificationDTO) throws ErrorService {
        try {
            NotificationDTO result = smsRepository.send(notificationDTO);
            notificationDTO.setNumSend(result.getNumSend());
            notificationDTO.setSendDate(result.getSendDate());
        } catch (ErrorClient ec) {
            throw new ErrorService("Fallo en el envío de SMS. Código: " + ec);
        }
    }
}
