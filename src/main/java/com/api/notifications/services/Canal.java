package com.api.notifications.services;


import com.api.notifications.errors.ErrorService;
import dto.NotificationDTO;

public interface Canal {


    String getName();
    public void send(NotificationDTO notificationDTO)  throws ErrorService;
}
