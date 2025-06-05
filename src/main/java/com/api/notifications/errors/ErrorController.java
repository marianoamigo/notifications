package com.api.notifications.errors;

import com.api.notifications.errors.ErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ErrorService.class)
    public ResponseEntity<String> manejarErrorService(ErrorService es) {
        String mensaje = es.getMessage();
        if(mensaje.contains("Token inválido") || mensaje.contains("Las credenciales son inválidas")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mensaje);
        } else if(mensaje.contains("No se encontró el usuario")
                || mensaje.contains("No se encontró la notificación")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensaje);
        } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErrorDesconocido(Exception ex) {
        // Excepciones a ignorar para que tome Swagger
        if (ex instanceof org.springframework.web.servlet.NoHandlerFoundException
                || ex instanceof org.springframework.web.HttpRequestMethodNotSupportedException
                || ex instanceof org.springframework.web.bind.MissingServletRequestParameterException) {
            throw (RuntimeException) ex; // dejás que Spring las maneje
        }


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + ex.getMessage());
    }
}
