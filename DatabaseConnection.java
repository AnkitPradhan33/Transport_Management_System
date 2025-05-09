import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DatabaseConnection {
    private static final String HOST = "localhost";
    private static final String USER = "root";
    private static final String PASSWORD = "Ankit@123";
    private static final String DATABASE = "transport_db";
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    public static Connection getConnection() throws SQLException {
        try {
            LOGGER.info("Loading MySQL JDBC driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?useSSL=false&allowPublicKeyRetrieval=true";
            LOGGER.info("Attempting to connect to database: " + url);

            Connection conn = DriverManager.getConnection(url, USER, PASSWORD);
            LOGGER.info("Successfully connected to database");
            return conn;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            throw e;
        }
    }
}