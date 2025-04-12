package main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database {
    public Connection getConnection(){
// ⚙️ Cambiá estos valores por los de tu entorno local
        String url = "jdbc:mysql://localhost:3306/notifications?serverTimezone=UTC";
        String user = "root";
        String password = "root";
        Connection connection = null;
        try {
            // 🟢 Intentamos conectar
            connection = DriverManager.getConnection(url, user, password);
            //System.out.println("✅ ¡Conexión exitosa a la base de datos!");

            // 🔒 Siempre cerramos la conexión cuando terminamos
            //connection.close();
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }
}
