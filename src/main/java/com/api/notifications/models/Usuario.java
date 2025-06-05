package com.api.notifications.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter @Column(name= "id")
    private Integer id;
    @Getter @Setter @Column(unique = true, name = "mail")
    private String mail;
    @Getter @Setter @Column(name= "pass")
    private String pass;

    public Usuario(){}
    public Usuario(String mail, String pass) {
        this.mail = mail;
        this.pass = pass;
    }
    public Usuario(Integer id,String mail, String pass) {
        this.id = id;
        this.mail = mail;
        this.pass = pass;
    }


}
