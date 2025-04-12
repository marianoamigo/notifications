package main;

public class Notification {
    private int idnotif;
    private String title;
    private String content;
    //private Chanel chanel;
    //private User iduser;

    public Notification(){

    }

    public Notification(int idnotif, String title, String content /*Chanel chanel, User iduser*/) {
        this.idnotif = idnotif;
        this.title = title;
        this.content = content;
        //this.chanel = chanel;
        //this.iduser = iduser;
    }

    public int getIdnotif() {
        return idnotif;
    }

    public void setIdnotif(int idnotif) {
        this.idnotif = idnotif;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    }




