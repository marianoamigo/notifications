package com.api.notifications.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    private String mail;
    @NotBlank(message = "La pass es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String pass;
    @NotBlank(message = "Debe ingresar la segunda contraseña")
    private String segundaPass;

    public RegisterRequest() {
    }
    public RegisterRequest(String mail, String pass, String segundaPass) {
        this.mail = mail;
        this.pass = pass;
        this.segundaPass = segundaPass;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSegundaPass() {
        return segundaPass;
    }

    public void setSegundaPass(String segundaPass) {
        this.segundaPass = segundaPass;
    }
}

