import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    public int addVehicle(String plateNumber, String model, String type, int capacity, Date purchaseDate,
            Date insuranceExpiry, String status) throws SQLException {
        String query = "INSERT INTO vehicles (plate_number, model, type, capacity, purchase_date, insurance_expiry, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, plateNumber);
            pstmt.setString(2, model);
            pstmt.setString(3, type);
            pstmt.setInt(4, capacity);
            pstmt.setDate(5, purchaseDate);
            pstmt.setDate(6, insuranceExpiry);
            pstmt.setString(7, status);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating vehicle failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating vehicle failed, no ID obtained.");
                }
            }
        }
    }

    public void updateVehicle(int vehicleId, String plateNumber, String model, String type, int capacity,
            Date purchaseDate, Date insuranceExpiry, String status) throws SQLException {
        String query = "UPDATE vehicles SET plate_number = ?, model = ?, type = ?, capacity = ?, purchase_date = ?, insurance_expiry = ?, status = ? WHERE vehicle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, plateNumber);
            pstmt.setString(2, model);
            pstmt.setString(3, type);
            pstmt.setInt(4, capacity);
            pstmt.setDate(5, purchaseDate);
            pstmt.setDate(6, insuranceExpiry);
            pstmt.setString(7, status);
            pstmt.setInt(8, vehicleId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating vehicle failed, no rows affected.");
            }
        }
    }

    public void deleteVehicle(int vehicleId) throws SQLException {
        String query = "DELETE FROM vehicles WHERE vehicle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, vehicleId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting vehicle failed, no rows affected.");
            }
        }
    }

    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("plate_number"),
                        rs.getString("model"),
                        rs.getString("type"),
                        rs.getInt("capacity"),
                        rs.getDate("purchase_date"),
                        rs.getDate("insurance_expiry"),
                        rs.getString("status"));
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleId) throws SQLException {
        String query = "SELECT * FROM vehicles WHERE vehicle_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, vehicleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehicle(
                            rs.getInt("vehicle_id"),
                            rs.getString("plate_number"),
                            rs.getString("model"),
                            rs.getString("type"),
                            rs.getInt("capacity"),
                            rs.getDate("purchase_date"),
                            rs.getDate("insurance_expiry"),
                            rs.getString("status"));
                }
            }
        }
        return null;
    }
}