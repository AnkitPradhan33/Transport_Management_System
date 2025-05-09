import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    public int addDriver(String name, String licenseNumber, String phone, String address, Date dob, int userId)
            throws SQLException {
        String query = "INSERT INTO drivers (name, license_number, phone, address, dob, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, licenseNumber);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setDate(5, dob);
            pstmt.setInt(6, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating driver failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating driver failed, no ID obtained.");
                }
            }
        }
    }

    public void updateDriver(int driverId, String name, String licenseNumber, String phone, String address, Date dob,
            int userId) throws SQLException {
        String query = "UPDATE drivers SET name = ?, license_number = ?, phone = ?, address = ?, dob = ?, user_id = ? WHERE driver_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, licenseNumber);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setDate(5, dob);
            pstmt.setInt(6, userId);
            pstmt.setInt(7, driverId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating driver failed, no rows affected.");
            }
        }
    }

    public void deleteDriver(int driverId) throws SQLException {
        String query = "DELETE FROM drivers WHERE driver_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, driverId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting driver failed, no rows affected.");
            }
        }
    }

    public List<Driver> getAllDrivers() throws SQLException {
        List<Driver> drivers = new ArrayList<>();
        String query = "SELECT * FROM drivers";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Driver driver = new Driver(
                        rs.getInt("driver_id"),
                        rs.getString("name"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getDate("dob"),
                        rs.getInt("user_id"));
                drivers.add(driver);
            }
        }
        return drivers;
    }

    public Driver getDriverById(int driverId) throws SQLException {
        String query = "SELECT * FROM drivers WHERE driver_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Driver(
                            rs.getInt("driver_id"),
                            rs.getString("name"),
                            rs.getString("license_number"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getDate("dob"),
                            rs.getInt("user_id"));
                }
            }
        }
        return null;
    }
}