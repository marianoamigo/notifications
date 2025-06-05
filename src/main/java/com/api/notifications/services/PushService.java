package com.api.notifications.services;
import com.api.notifications.client.PushClient;
import com.api.notifications.errors.ErrorClient;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.repositories.PushRepository;
import com.api.notifications.utils.JWTUtil;
import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PushService implements Canal {

    private final PushRepository pushRepository;

    @Autowired
    public PushService(PushRepository pushRepository) {
        this.pushRepository = pushRepository;
    }

    @Autowired
    JWTUtil jwtUtil;

    @Override
    public String getName() {
        return "PUSH";
    }

    @Override
    public void send(NotificationDTO notificationDTO) throws ErrorService {
        try {
            NotificationDTO result = pushRepository.send(notificationDTO);
            notificationDTO.setSendState(result.isSendState());
            notificationDTO.setNumSend(result.getNumSend());
            notificationDTO.setSendDate(result.getSendDate());
            notificationDTO.setTokenDevice(result.getTokenDevice());
        } catch (ErrorClient ec) {
            throw new ErrorService("No se pudo enviar notificacion por push", ec);
        }

    }
}

