package applications;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://195.235.211.197/piizensorium";
    private static final String USERNAME = "piizensorium";
    private static final String PASSWORD = "Zensorium@2024";

    public static Connection obtenerConexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error en la conexión a la base de datos: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}