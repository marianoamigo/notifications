package com.api.notifications.services;

import com.api.notifications.errors.ErrorService;
import com.api.notifications.models.Notification;
import com.api.notifications.models.Usuario;
import com.api.notifications.repositories.INotificationRepository;
import com.api.notifications.repositories.IUserRepository;
import com.api.notifications.utils.JWTUtil;
import dto.NotificationDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private INotificationRepository notificationRepository;
    private final ChannelFactory channelFactory;


    public NotificationService(
            ChannelFactory channelFactory,
            IUserRepository userRepository,
            INotificationRepository notificationRepository,
            JWTUtil jwtUtil
    ) {
        this.channelFactory = channelFactory;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void sendNotification(NotificationDTO notificationDTO, String token) throws ErrorService {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        Integer userId = Integer.valueOf(validado);
        validateNotification(notificationDTO);
        Notification notification = new Notification();
        Canal canalCorrecto = channelFactory.getChannel(notificationDTO.getChannel());
        Optional<Usuario> respuesta = userRepository.findById(userId);
        if(!respuesta.isPresent()) {
            throw new ErrorService("No se encontró el usuario");
        }
        if(canalCorrecto!= null) {
            notification.setTokenDevice(jwtUtil.create("id", "subject")); //token falso para envio PUSH
            notificationDTO.setTokenDevice(notification.getTokenDevice());

            canalCorrecto.send(notificationDTO);

            setAll(notification,notificationDTO,canalCorrecto);

            notification.setUser(respuesta.get());
        } else {
            throw new ErrorService("Debe elegir un canal válido");
        }
        notificationRepository.save(notification);
    }



    public Notification getNotificationById(Integer id, String token)  throws  ErrorService{
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        Optional <Notification> notificacionOptional = notificationRepository.findById(id);
        if(notificacionOptional.isPresent()) {
           Notification notificationExistente = notificacionOptional.get();
           if (validado.equals(String.valueOf(notificationExistente.getUser().getId()))) {
               return notificationExistente;
           } else throw new ErrorService("Token inválido");
        } else throw new ErrorService("No se encontró la notificación");
    }

    public List<Notification> getNotificationsByUser(String token) {
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        return notificationRepository.findByUser_Id(Integer.valueOf(validado));
    }


    @Transactional
    public void modifyNotification(NotificationDTO notificacionDTO, Integer idNoti, String token) throws ErrorService{
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        Integer idUser = Integer.valueOf(validado);
        Optional<Notification> respuesta = notificationRepository.findById(idNoti);
        if (!respuesta.isPresent()) {
            System.out.println("DEBUG - No se encontró notificación con id: " + idNoti);
            throw new ErrorService("No se encontró la notificación");
        }
        Notification notificationExistente = respuesta.get();

        validateNotification(notificacionDTO);
        validateUser(notificationExistente.getUser().getId(), idUser);

        Canal canalCorrecto = channelFactory.getChannel(notificacionDTO.getChannel());
        if(canalCorrecto!= null) {
            notificacionDTO.setTokenDevice(jwtUtil.create("id nueva", "subject nueva"));

            canalCorrecto.send(notificacionDTO);

            setAll(notificationExistente,notificacionDTO,canalCorrecto);
        } else throw new ErrorService("Debe elegir un canal válido");
        notificationRepository.save(notificationExistente);
        }


    @Transactional
    public void delete(Integer idNoti, String token) throws ErrorService{
        token = jwtUtil.acortaToken(token);
        String validado = jwtUtil.validaToken(token);
        Integer idUser = Integer.valueOf(validado);
        Optional<Notification> respuesta = notificationRepository.findById(idNoti);
        if(respuesta.isPresent()) {
            Notification notificationABorrar = respuesta.get();
            validateUser(notificationABorrar.getUser().getId(), idUser);
            notificationRepository.delete(notificationABorrar);
        } else throw new ErrorService("No se encontró la notificación") ;
    }

    private void validateNotification(NotificationDTO notificacionDTO) throws ErrorService {
        if (notificacionDTO.getTitle() == null || notificacionDTO.getTitle().isEmpty()) {
            throw new ErrorService("El titulo no puede ser nulo");
        }
        if (notificacionDTO.getBody() == null || notificacionDTO.getBody().isEmpty()) {
            throw new ErrorService("El cuerpo no puede ser nulo");
        }
    }

    private void validateUser(Integer idEncontrado, Integer idEnviado) throws ErrorService{
        System.out.println("Id del user encontrado: "+ idEncontrado);
        System.out.println("Id del user enviado: "+ idEnviado);
        if(!idEnviado.equals(idEncontrado)) {
            throw new ErrorService("El usuario no coincide con el de la notificacion deseada");
        }
    }

    private void setAll(Notification notification, NotificationDTO notificacionDTO, Canal canalCorrecto) {
        notification.setTitle(notificacionDTO.getTitle());
        notification.setBody(notificacionDTO.getBody());
        notification.setChannel(canalCorrecto.getName());
        notification.setTokenDevice(notificacionDTO.getTokenDevice());
        notification.setSendState(notificacionDTO.isSendState());
        notification.setNumSend(notificacionDTO.getNumSend());
        notification.setSendDate(notificacionDTO.getSendDate());
        notification.setRecipient(notificacionDTO.getRecipient());
    }


}
