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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    public void registrarCuandoCredencialesSonValidas() throws ErrorService {
        RegisterRequest request = new RegisterRequest("test@mail.com","12345678","12345678");
        String hashed="hashed_pass";
        when(passwordEncoder.encode("12345678")).thenReturn(hashed);
        //
        userService.register(request);
        //
        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getMail().equals("test@mail.com") &&
                usuario.getPass().equals("hashed_pass")
        ));
    }

    @Test
    public void registrarFallaCuandoCredencialesNoCoinciden() throws ErrorService{
        RegisterRequest request = new RegisterRequest("test@mail.com","12345678","123456789");
        ErrorService es = assertThrows(ErrorService.class, () ->{
            userService.register(request);
        });
        assertEquals("Las contraseñas no coinciden", es.getMessage());
    }

    @Test
    public void registrarFallaCuandoMailNoTieneArroba() throws ErrorService {
        RegisterRequest request = new RegisterRequest("test", "12345678", "123456789");
        ErrorService es = assertThrows(ErrorService.class, () -> {
            userService.register(request);
        });
        assertEquals("El mail no puede ser nulo, y debe ser con formato ejemplo@ejemplo.com", es.getMessage());

    }

    @Test
    public void registrarFallaCuandoPassTieneMenosDigitos() throws ErrorService{
        RegisterRequest request = new RegisterRequest("test@mail.com","1234567","1234567");
        ErrorService es = assertThrows(ErrorService.class, () ->{
            userService.register(request);
        });
        assertEquals("La contraseña no puede ser nula y debe tener mas de 8 digitos", es.getMessage());
    }


    @Test
    public void loginCuandoCredencialesSonValidas() throws ErrorService{
        LoginRequest request = new LoginRequest("test@mail.com","12345678");

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(2, 65536, 1, "12345678");
        Usuario mock = new Usuario(1,"test@mail.com",hash);

        when(usuarioRepository.findByMail(request.getMail())).thenReturn(Optional.of(mock));
        when(jwtUtil.create(String.valueOf(1),"test@mail.com")).thenReturn("fake_token");

        String token = userService.login(request);

        assertEquals("fake_token", token);
    }
    @Test
    public void loginFallaCuandoLaPassNoEsValida() throws ErrorService{
        LoginRequest request = new LoginRequest("test@mail.com","123456789");

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(2, 65536, 1, "12345678");
        Usuario mock = new Usuario(1,"test@mail.com",hash);

        when(usuarioRepository.findByMail(request.getMail())).thenReturn(Optional.of(mock));

        ErrorService es = assertThrows(ErrorService.class, () ->{
            String token = userService.login(request);
        });

        assertEquals("Las credenciales son inválidas", es.getMessage());
    }

    @Test
    public void modificarCuandoCredencialesSonValidas() throws ErrorService{
        RegisterRequest request = new RegisterRequest("modificado@mail.com", "12345678", "12345678");
        Integer userId = 1;
        String token = "token";

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(2, 65536, 1, "12345678");
        Usuario mock = new Usuario(1,"test@mail.com",hash);

        when(jwtUtil.validaToken(Mockito.any())).thenReturn("1");
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(mock));
        when(passwordEncoder.encode("12345678")).thenReturn(hash);


        //llama al método
        userService.modificar(userId,request, token);

        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getMail().equals("modificado@mail.com") &&
                usuario.getPass().equals(hash)
        ));
    }

    @Test
    public void modificarFallaCuandoTokenEsInvalido() throws ErrorService{
        RegisterRequest request = new RegisterRequest("modificado@mail.com", "12345678", "12345678");
        Integer userId = 2;
        String token = "token";

        when(jwtUtil.validaToken(Mockito.any())).thenReturn("1");

        //llama al método
        ErrorService es = assertThrows(ErrorService.class, () ->{
            userService.modificar(userId,request, token);
        });

        assertEquals("Token inválido", es.getMessage());
    }

    @Test
    public void verUsuarioPorId() throws ErrorService{
        Integer userId = 2;
        String token = "token";
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 65536, 1, "12345678");
        Usuario mock = new Usuario(2,"encontrado@mail.com",hash);

        when(jwtUtil.validaToken(Mockito.any())).thenReturn("2");
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(mock));

        Usuario encontrado = userService.verUsuarioPorId(userId, token);

        assertEquals(2,encontrado.getId());
        assertEquals("encontrado@mail.com",encontrado.getMail());
    }



}
