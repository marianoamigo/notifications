package com.api.notifications.repositories;

import com.api.notifications.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IUserRepository extends JpaRepository<Usuario, Integer> {
//    Usuario findByMail(String mail);

    Optional<Usuario> findByMail(String mail);
}
