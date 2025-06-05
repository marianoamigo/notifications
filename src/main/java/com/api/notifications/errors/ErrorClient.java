package com.api.notifications.errors;

public class ErrorClient extends RuntimeException{
    public ErrorClient(String message) {
        super(message);
    }

    public ErrorClient(String message, Throwable cause) {
        super(message, cause);
    }
}
