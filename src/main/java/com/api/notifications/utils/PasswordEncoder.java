package com.api.notifications.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String encode(String password) {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        return argon2.hash(1, 1024, 1, password); //(cantIteraciones,espacioUsado,cantIterALaVez,textoAEncriptar)
    } //devuelve la pass hasheada
}
