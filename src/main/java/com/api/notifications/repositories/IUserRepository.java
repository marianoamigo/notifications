package com.api.notifications.repositories;

import com.api.notifications.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<UserModel, Integer> {
//    Usuario findByMail(String mail);

    Optional<UserModel> findByMail(String mail);
}
