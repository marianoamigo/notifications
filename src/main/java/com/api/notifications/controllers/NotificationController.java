package com.api.notifications.controllers;

import com.api.notifications.models.NotificationModel;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.services.NotificationService;
import dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestHeader(value="Authorization") String token, @RequestBody NotificationDTO notificationDTO) throws ErrorService {
        notificationService.sendNotification(notificationDTO, token);
        return ResponseEntity.status(HttpStatus.CREATED).body("Notificacion creada exitosamente");
    }

    @GetMapping("/notification/{idNoti}")
    public ResponseEntity<NotificationModel> getNotification(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token) throws ErrorService {
        NotificationModel notificationModel = notificationService.getNotificationById(idNoti, token);
        return ResponseEntity.ok(notificationModel);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getNotificationsByUser(@RequestHeader(value="Authorization") String token) {
        List<NotificationModel> notificationModels = notificationService.getNotificationsByUser(token);
        if(notificationModels == null || notificationModels.isEmpty()) {
            return ResponseEntity.ok("El usuario no tiene notificaciones");
        }
        return ResponseEntity.ok(notificationModels);
    }


    @PutMapping("/modify/{idNoti}")
    public ResponseEntity<?> modifyNotifications(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token, @RequestBody NotificationDTO notificationDTO) throws ErrorService {
        notificationService.modifyNotification(notificationDTO, idNoti, token);
        return ResponseEntity.ok("Notificacion modificada exitosamente");
    }



    @DeleteMapping("/delete/{idNoti}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token) throws ErrorService{
        notificationService.delete(idNoti, token);
        return ResponseEntity.ok("Notificación eliminada");
    }
}






