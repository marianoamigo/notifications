package com.api.notifications.controllers;

import com.api.notifications.models.Notification;
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

    @GetMapping("/notificacion/{idNoti}")
    public ResponseEntity<Notification> getNotification(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token) throws ErrorService {
        Notification notification = notificationService.verNotificacionPorId(idNoti, token);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/todas")
    public ResponseEntity<?> getNotificationsByUser(@RequestHeader(value="Authorization") String token) {
        List<Notification> notifications = notificationService.verNotificacionesPorUsuario(token);
        if(notifications == null || notifications.isEmpty()) {
            return ResponseEntity.ok("El usuario no tiene notificaciones");
        }
        return ResponseEntity.ok(notifications);
    }


    @PutMapping("/modificar/{idNoti}")
    public ResponseEntity<?> modifyNotifications(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token, @RequestBody NotificationDTO notificationDTO) throws ErrorService {
        notificationService.modifyNotification(notificationDTO, idNoti, token);
        return ResponseEntity.ok("Notificacion modificada exitosamente");
    }



    @DeleteMapping("/eliminar/{idNoti}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer idNoti, @RequestHeader(value="Authorization") String token) throws ErrorService{
        notificationService.delete(idNoti, token);
        return ResponseEntity.ok("Notificación eliminada");
    }
}






