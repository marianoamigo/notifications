package com.api.notifications.services;

import com.api.notifications.dtos.LoginRequest;
import com.api.notifications.dtos.RegisterRequest;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.models.Usuario;
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
    private IUserRepository usuarioRepository;

    @Transactional
    public void register(RegisterRequest request) throws ErrorService {
        validarCredenciales(request);
        String hash = passwordEncoder.encode(request.getPass());
        request.setPass(hash);
        Usuario nuevo = new Usuario(request.getMail(),request.getPass());
        usuarioRepository.save(nuevo);
    }

    public String login(LoginRequest loginRequest) throws ErrorService {
        Usuario usuarioLogueado = obtenerUsuarioPorCredenciales(loginRequest);
        String token = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getMail());
        return token;
    }

    public Usuario verUsuarioPorId(Integer id, String token)  throws ErrorService {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        if (validado.equals(String.valueOf(id))) {
            Optional<Usuario> respuesta = usuarioRepository.findById(id);
            if(!respuesta.isEmpty()){
            Usuario encontrado = respuesta.get();
            return encontrado;
            } else throw new ErrorService("No se encontró el usuario con ID: " + id);
        } else throw new ErrorService("Token inválido");
    }

    @Transactional
    public void modificar(Integer id, RegisterRequest request, String token) throws ErrorService {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        if (validado.equals(String.valueOf(id))) {
            validarCredenciales(request);
            String hash = passwordEncoder.encode(request.getPass());
            Optional<Usuario> respuesta = usuarioRepository.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuarioExistente = respuesta.get();
                usuarioExistente.setMail(request.getMail());
                usuarioExistente.setPass(hash);
                usuarioRepository.save(usuarioExistente);
            } else throw new ErrorService("No se encontró el usuario con ID: " + id);
        } else throw new ErrorService("Token inválido");
    }

    public Usuario obtenerUsuarioPorCredenciales(LoginRequest loginRequest) throws ErrorService {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByMail(loginRequest.getMail());
        if (optionalUsuario.isPresent()) {
            Usuario usuarioExistente = optionalUsuario.get();
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            boolean verificado = argon2.verify(usuarioExistente.getPass(),loginRequest.getPass());
            if(verificado) {
                return usuarioExistente;
            } else {
                throw new ErrorService("Las credenciales son inválidas");
            }
        } else {
            throw new ErrorService("Las credenciales son inválidas");
        }
    }

    public void validarCredenciales(RegisterRequest request) throws ErrorService {
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
