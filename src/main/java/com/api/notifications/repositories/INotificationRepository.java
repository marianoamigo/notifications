package com.api.notifications.repositories;

import com.api.notifications.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> findByUsuario_Id(Integer userId);

    Optional<Notification> findById(Integer id);
}
