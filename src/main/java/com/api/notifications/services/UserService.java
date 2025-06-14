package com.api.notifications.services;

import com.api.notifications.dtos.LoginRequest;
import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.models.UserModel;
import com.api.notifications.repositories.IUserRepository;
import com.api.notifications.utils.JWTUtil;
import com.api.notifications.utils.PasswordEncoder;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Transactional
    public void register(RegisterRequest request) throws ErrorService {
        validateCredentials(request);
        String hash = passwordEncoder.encode(request.getPass());
        request.setPass(hash);
        UserModel nuevo = new UserModel(request.getMail(),request.getPass());
        userRepository.save(nuevo);
    }

    public String login(LoginRequest loginRequest) throws ErrorService {
        UserModel userLogueado = getUserByCredentials(loginRequest);
        String token = jwtUtil.create(String.valueOf(userLogueado.getId()), userLogueado.getMail());
        return token;
    }

    public UserModel getUserById(Integer id, String token)  throws ErrorService {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        if (validado.equals(String.valueOf(id))) {
            Optional<UserModel> respuesta = userRepository.findById(id);
            if(!respuesta.isEmpty()){
            UserModel encontrado = respuesta.get();
            return encontrado;
            } else throw new ErrorService("No se encontró el usuario con ID: " + id);
        } else throw new ErrorService("Token inválido");
    }

    @Transactional
    public void modify(Integer id, RegisterRequest request, String token) throws ErrorService {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        if (validado.equals(String.valueOf(id))) {
            validateCredentials(request);
            String hash = passwordEncoder.encode(request.getPass());
            Optional<UserModel> respuesta = userRepository.findById(id);
            if (respuesta.isPresent()) {
                UserModel userModelExistente = respuesta.get();
                userModelExistente.setMail(request.getMail());
                userModelExistente.setPass(hash);
                userRepository.save(userModelExistente);
            } else throw new ErrorService("No se encontró el usuario con ID: " + id);
        } else throw new ErrorService("Token inválido");
    }

    public UserModel getUserByCredentials(LoginRequest loginRequest) throws ErrorService {
        Optional<UserModel> optionalUser = userRepository.findByMail(loginRequest.getMail());
        if (optionalUser.isPresent()) {
            UserModel userFound = optionalUser.get();
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            boolean verified = argon2.verify(userFound.getPass(),loginRequest.getPass());
            if(verified) {
                return userFound;
            } else {
                throw new ErrorService("Las credenciales son inválidas");
            }
        } else {
            throw new ErrorService("Las credenciales son inválidas");
        }
    }

    public void validateCredentials(RegisterRequest request) throws ErrorService {
        if (request.getMail() == null || request.getMail().isEmpty() || !request.getMail().contains("@")) {
            throw new ErrorService("El mail no puede ser nulo, y debe ser con formato ejemplo@ejemplo.com");
        }
        if (request.getPass() == null || request.getPass().isEmpty() || request.getPass().length() < 8) {
            throw new ErrorService("La contraseña no puede ser nula y debe tener mas de 8 digitos");
        }
        if (!request.getPass().equals(request.getSegundaPass())) {
            throw new ErrorService("Las contraseñas no coinciden");
        }
    }

}
