package main;

import java.sql.Connection;

public class Main {
    public static void main(String[] args){
        Database db = new Database();

        try (Connection c = db.get_connection()){

        } catch (Exception e){
            System.out.println(e);
        }
    }
}
