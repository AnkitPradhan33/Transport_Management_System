import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public int addBooking(int userId, int vehicleId, Date bookingDate, String status, String remarks)
            throws SQLException {
        String query = "INSERT INTO booking (user_id, vehicle_id, booking_date, status, remarks) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, vehicleId);
            pstmt.setDate(3, bookingDate);
            pstmt.setString(4, status);
            pstmt.setString(5, remarks);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating booking failed, no ID obtained.");
                }
            }
        }
    }

    public void updateBooking(int bookingId, int userId, int vehicleId, Date bookingDate, String status, String remarks)
            throws SQLException {
        String query = "UPDATE booking SET user_id = ?, vehicle_id = ?, booking_date = ?, status = ?, remarks = ? WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, vehicleId);
            pstmt.setDate(3, bookingDate);
            pstmt.setString(4, status);
            pstmt.setString(5, remarks);
            pstmt.setInt(6, bookingId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating booking failed, no rows affected.");
            }
        }
    }

    public void deleteBooking(int bookingId) throws SQLException {
        String query = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting booking failed, no rows affected.");
            }
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM booking";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("user_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("booking_date"),
                        rs.getString("status"),
                        rs.getString("remarks"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String query = "SELECT * FROM booking WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(
                            rs.getInt("booking_id"),
                            rs.getInt("user_id"),
                            rs.getInt("vehicle_id"),
                            rs.getDate("booking_date"),
                            rs.getString("status"),
                            rs.getString("remarks"));
                }
            }
        }
        return null;
    }
}