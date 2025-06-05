package com.api.notifications.services;

import com.api.notifications.client.EmailClient;
import com.api.notifications.client.PushClient;
import com.api.notifications.client.SmsClient;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.models.Notification;
import com.api.notifications.models.Usuario;
import com.api.notifications.repositories.*;
import com.api.notifications.utils.JWTUtil;
import dto.NotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock private IUserRepository usuarioRepository;
    @Mock private INotificationRepository notificacionRepository;
    @Mock private JWTUtil jwtUtil;

    @Mock private ChannelFactory channelFactory;
    @Mock private EmailClient emailClient;
    @Mock private PushClient pushClient;
    @Mock private SmsClient smsClient;

    private EmailRepository emailRepository;
    private SmsRepository smsRepository;
    private PushRepository pushRepository;


    private NotificationService notificationService;


    @BeforeEach
    public void setUp() {
        //Instancia real de canales, con client mockeados
        Canal emailCanal = new EmailService(emailRepository);
        Canal smsCanal = new SmsService(smsRepository);
        Canal pushCanal = new PushService(pushRepository);
        notificationService = new NotificationService(
                new ChannelFactory(List.of(emailCanal,smsCanal,pushCanal)),
                usuarioRepository,
                notificacionRepository,
                jwtUtil
        );
    }



    @Test
    public void enviarEmailCorrectamente() throws ErrorService {
        NotificationDTO notificacionDTO = new NotificationDTO();
        String token = "token";

        //dto original
        notificacionDTO.setTitle("Titulo Mock");
        notificacionDTO.setBody("Cuerpo Mock");
        notificacionDTO.setChannel("email");
        //atributos del mock de jwtUtil
        Integer idUser = 1;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");

        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        resultadoClient.setTitle("Titulo Mock"); //no deberian cambiar en el microservicio
        resultadoClient.setBody("Cuerpo Mock");//
        resultadoClient.setChannel("email");//
        resultadoClient.setRecipient("ejemplo@mail.com");
        resultadoClient.setNumSend(100);
        resultadoClient.setSendDate(LocalDateTime.now());
        resultadoClient.setTokenDevice("token_fake_recibido");

        //mocks token y userRepo
        when(jwtUtil.validaToken(Mockito.any())).thenReturn("1");
        when(usuarioRepository.findById(idUser)).thenReturn(Optional.of(mock));
        when(jwtUtil.create("id","subject")).thenReturn("fake_token");



        //mockear ResponseEntity de microservicio
        ResponseEntity<NotificationDTO> fakeResponse = new ResponseEntity<>(resultadoClient, HttpStatus.CREATED);
        when(emailClient.send(any(NotificationDTO.class))).thenReturn(fakeResponse);

        //llamado al método a testear
        notificationService.sendNotification(notificacionDTO, token);

        //verificaciones
        verify(emailClient).send(any(NotificationDTO.class));
        verify(notificacionRepository).save(argThat(notificacion ->
                notificacion.getTitle().equals("Titulo Mock") &&
                        notificacion.getBody().equals("Cuerpo Mock") &&
                        notificacion.getChannel().equalsIgnoreCase("EMAIL") &&
                        notificacion.getRecipient().equals("ejemplo@mail.com") &&
                        notificacion.getTokenDevice().equals("token_fake_recibido") &&
                        (notificacion.getNumSend()==100)
        ));
    } //test pasa



    @Test
    public void enviarNotiConCanalIncorrectoFalla() throws ErrorService{
        NotificationDTO notificacionDTO = new NotificationDTO();
        String token = "token";

        //dto original
        notificacionDTO.setTitle("Titulo Mock");
        notificacionDTO.setBody("Cuerpo Mock");
        notificacionDTO.setChannel("otro");

        //atributos del mock de jwtUtil
        Integer idUser = 1;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");

        when(jwtUtil.validaToken(Mockito.any())).thenReturn("1");
        when(usuarioRepository.findById(idUser)).thenReturn(Optional.of(mock));


        //llamado al método a testear
        ErrorService es = assertThrows(ErrorService.class, () ->{
            notificationService.sendNotification(notificacionDTO,token);
                });

        assertEquals("Debe elegir un canal válido", es.getMessage());

        verify(notificacionRepository, never()).save(any());

    }

    @Test
    public void verNotificacionPorId() throws ErrorService {
        String token = "token";
        Integer idNoti = 1;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");
        Notification encontrado = new Notification();
        encontrado.setId(1);
        encontrado.setTitle("Titulo Mock");
        encontrado.setBody("Cuerpo Mock");
        encontrado.setChannel("email");
        encontrado.setUser(mock);

        when(jwtUtil.validaToken(Mockito.any())).thenReturn("1");
        when(notificacionRepository.findById(1)).thenReturn(Optional.of(encontrado));

        Notification notification = notificationService.verNotificacionPorId(idNoti, token);

        assertEquals(1,encontrado.getId());
        assertEquals("Titulo Mock",encontrado.getTitle());
        assertEquals("Cuerpo Mock",encontrado.getBody());
        assertEquals("email",encontrado.getChannel());

    }

    @Test
    public void modificarEmailCorrectamente() throws ErrorService{
        Integer idUser = 1;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");
        //notificacion que va a traer el repository
        Notification encontrado = new Notification();
        Integer idNoti = 1;

        encontrado.setTitle("Titulo Mock");
        encontrado.setBody("Cuerpo Mock");
        encontrado.setChannel("email");
        encontrado.setUser(mock);

        //dto original
        NotificationDTO notificacionDTO = new NotificationDTO();
        notificacionDTO.setTitle("Titulo Mock");
        notificacionDTO.setBody("Cuerpo Mock");
        notificacionDTO.setChannel("email");

        //atributos del mock de jwtUtil


        //simular lo que se recibe de microservicio
        NotificationDTO resultadoClient = new NotificationDTO();
        resultadoClient.setTitle("Titulo Mock"); //no deberian cambiar en el microservicio
        resultadoClient.setBody("Cuerpo Mock");//
        resultadoClient.setChannel("email");//
        resultadoClient.setRecipient("ejemplo@mail.com");
        resultadoClient.setNumSend(100);
        resultadoClient.setSendDate(LocalDateTime.now());
        resultadoClient.setTokenDevice("token_fake_recibido");

        //mocks: token , userRepository y notificacionRepository
        when(jwtUtil.create("id nueva","subject nueva")).thenReturn("fake_token");
        when(notificacionRepository.findById(idNoti)).thenReturn(Optional.of(encontrado));

        //mockear ResponseEntity de microservicio
        ResponseEntity<NotificationDTO> fakeResponse = new ResponseEntity<>(resultadoClient, HttpStatus.CREATED);
        when(emailClient.send(any(NotificationDTO.class))).thenReturn(fakeResponse);

        //llamado al método a testear
//        notificacionService.modificarNotificacion(notificacionDTO, idNoti,idUser);

        //verificaciones
        verify(emailClient).send(any(NotificationDTO.class));
        verify(notificacionRepository).save(argThat(notificacion ->
                notificacion.getTitle().equals("Titulo Mock") &&
                        notificacion.getBody().equals("Cuerpo Mock") &&
                        notificacion.getChannel().equalsIgnoreCase("EMAIL") &&
                        notificacion.getRecipient().equals("ejemplo@mail.com") &&
                        notificacion.getTokenDevice().equals("token_fake_recibido") &&
                        (notificacion.getNumSend()==100)
        ));
    } //test pasa


    @Test
    public void eliminarNotiCorrectamente() throws ErrorService{
        Integer idUser = 1;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");
        Notification encontrado = new Notification();
        Integer idNoti = 1;
        encontrado.setId(idNoti);
        encontrado.setTitle("Titulo Mock");
        encontrado.setBody("Cuerpo Mock");
        encontrado.setChannel("email");
        encontrado.setUser(mock);

        when(notificacionRepository.findById(idNoti)).thenReturn(Optional.of(encontrado));

        //llamado al método a testear
//        notificacionService.eliminarNotificacion(idNoti, idUser);

        //verificaciones
        verify(notificacionRepository).delete(argThat(notificacion ->
                notificacion.getTitle().equals("Titulo Mock") &&
                        notificacion.getBody().equals("Cuerpo Mock") &&
                        notificacion.getChannel().equalsIgnoreCase("EMAIL")
        ));
    } //test pasa

    @Test
    public void eliminarNotiFalla() throws ErrorService{
        Integer idUser = 2;
        Usuario mock = new Usuario(1, "test@mail.com", "12345678");
        Notification encontrado = new Notification();
        Integer idNoti = 1;
        encontrado.setId(idNoti);
        encontrado.setUser(mock);

        when(notificacionRepository.findById(idNoti)).thenReturn(Optional.of(encontrado));

        //llamado al método a testear
        ErrorService es = assertThrows(ErrorService.class, () ->{
//            notificacionService.eliminarNotificacion(idNoti, idUser);
        });

        assertEquals("El usuario no coincide con el de la notificacion deseada", es.getMessage());

        verify(notificacionRepository, never()).delete(any());
    } //test pasa

}
