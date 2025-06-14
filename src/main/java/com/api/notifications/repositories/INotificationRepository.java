package com.api.notifications.repositories;

import com.api.notifications.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<NotificationModel, Integer> {

    List<NotificationModel> findByUser_Id(Integer userId);

    Optional<NotificationModel> findById(Integer id);
}
