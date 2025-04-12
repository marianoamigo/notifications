package main;

import java.util.Scanner;

public class NotificationService {
    Scanner scanner = new Scanner(System.in);
    public static void createNotification(){
        Scanner scanner = new Scanner(System.in);
        int id = (int) (Math.random()*1000);
        System.out.println("Escribe tu titulo");
        String title = scanner.nextLine();
        System.out.println("Escribe el contenido");
        String content = scanner.nextLine();

        Notification register = new Notification();
        register.setIdnotif(id);
        register.setTitle(title);
        register.setContent(content);

        NotificationDAO.createNotificationDB(register);
    }

    public static void readNotification(){
        NotificationDAO.readNotificationDB();

    }
    public static void updateNotification(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the id of the notification you want to update");
        int idnot = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Write the new title");
        String title = scanner.nextLine();
        System.out.println("Write the new content");
        String content = scanner.nextLine();

        Notification update = new Notification();
        update.setIdnotif(idnot);
        update.setTitle(title);
        update.setContent(content);

        NotificationDAO.updateNotificationDB(update);

    }
    public static void deleteNotification(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the id to delete the notification");
        int idnot = scanner.nextInt();
        NotificationDAO.deleteNotification(idnot);

    }



}
