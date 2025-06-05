package com.api.notifications.errors;

public class ErrorService extends Exception{

    //se crea esta clase para diferenciar errores propios de bugs y errores propios del sistema
    public ErrorService(String message) {
        super(message);
    }

    public ErrorService(String message, Throwable cause) {
        super(message, cause);
    }
}
