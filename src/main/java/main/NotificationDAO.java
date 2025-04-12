package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationDAO {
    //se conecta a la DB

    public static void createNotificationDB(Notification notification){
        Database dbconnect = new Database();

        try (Connection connection= dbconnect.getConnection()){
            PreparedStatement ps = null;
            try{
                String query = "INSERT INTO `notification` (`idnotification`, `title`, `content`) VALUES (?, ?, ?)";
                System.out.println("¿Conexión cerrada?: " + connection.isClosed());
                ps = connection.prepareStatement(query);
                ps.setInt(1, notification.getIdnotif());
                ps.setString(2, notification.getTitle());
                ps.setString(3, notification.getContent());
                ps.executeUpdate();
                System.out.println("Notification created succesfully");
            } catch (SQLException e){
                System.out.println(e);
            }
        }catch (SQLException e){
            System.out.println(e);
        }

    }
    public static void readNotificationDB(){
        Database dbconnect = new Database();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try (Connection connection= dbconnect.getConnection()){
            String query = "SELECT * FROM `notification`";
            ps=connection.prepareStatement(query);
            rs=ps.executeQuery();
            while(rs.next()){
                System.out.println("ID: "+rs.getInt("idnotification"));
                System.out.println("TITLE: "+rs.getString("title"));
                System.out.println("CONTENT: "+rs.getString("content"));
            }
        }catch (SQLException e){
            System.out.println("Unable to reach the notifications");
            System.out.println(e);
        }

    }
    public static void updateNotificationDB(Notification notification){
        Database dbconnect = new Database();
        try (Connection connection= dbconnect.getConnection()) {
            PreparedStatement ps = null;
            try {
                String query="UPDATE `notification` SET `title` = ?, `content` = ? WHERE (`idnotification` = ?)";
                ps = connection.prepareStatement(query);
                ps.setString(1, notification.getTitle());
                ps.setString(2, notification.getContent());
                ps.setInt(3, notification.getIdnotif());
                ps.executeUpdate();
                System.out.println("Notification updated successfully");
            } catch (SQLException e) {
                System.out.println("Unable to update the notifications");
                System.out.println(e);
            }
        } catch (SQLException e) {
                System.out.println(e);
        }

    }
    public static void deleteNotification(int idnotif){
        Database dbconnect = new Database();

        try (Connection connection= dbconnect.getConnection()){
            PreparedStatement ps = null;
            try {
                String query="DELETE FROM `notification` WHERE (`idnotification` = ?);";
                ps=connection.prepareStatement(query);
                ps.setInt(1, idnotif);
                ps.executeUpdate();
                System.out.println("Notification deleted successfully");

            }catch (SQLException e) {
                System.out.println("Unable to delete the notifications");
                System.out.println(e);
            }
        } catch (SQLException e){
            System.out.println(e);
        }
    }
}
