package com.api.notifications.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name="notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter @Column(name= "id")
    private Integer id;
    @Getter @Setter @Column(name= "title")
    private String title;
    @Getter @Setter @Column(name= "content")
    private String body;
    @Getter @Setter @Column(name= "channel")
    private String channel;
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
    @Getter @Setter @Column(name= "destination")
    private String recipient;
    @Getter @Setter @Column(name= "num_send")
    private int numSend;
    @Getter @Setter @Column(name= "date_send")
    private LocalDateTime sendDate; //despues se setea con new Date
    @Getter @Setter @Column(name= "state_send")
    private boolean sendState;
    @Getter @Setter @Column(name="token_test") //token falso solo para simular el envio push
    private String tokenDevice;
}
