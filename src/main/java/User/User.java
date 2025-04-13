package User;

import Notification.Notification;

import java.util.ArrayList;

public class User {
    private int iduser;
    private String email;
    private String pass;
    private ArrayList<Notification> notifications;


    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }



    public ArrayList<Notification> getNotifications() {

        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {

        this.notifications = notifications;
    }




}
