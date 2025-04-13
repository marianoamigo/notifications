package main;

import Notification.NotificationService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        //hacemos un menú para que arranque.
        int option=0;

        do {
            System.out.println("--------------------");
            System.out.println("Notifications App");
            System.out.println("1. Create Notification");
            System.out.println("2. List Notifications");
            System.out.println("3. Edit Notification");
            System.out.println("4. Delete Notification");
            System.out.println("5. Exit");
            option = scanner.nextInt();

            switch (option){
                case 1:
                    NotificationService.createNotification();
                    break;
                case 2:
                    NotificationService.readNotification();
                    break;
                case 3:
                    NotificationService.updateNotification();
                    break;
                case 4:
                    NotificationService.deleteNotification();
                    break;
                default:
                    break;
            }

        } while (option != 5);


        /*Database db = new Database();

        try (Connection c = db.get_connection()){

        } catch (Exception e){
            System.out.println(e);
        }*/

    }
}
