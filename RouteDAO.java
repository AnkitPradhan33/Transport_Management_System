import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    public int addRoute(String origin, String destination, float distanceKm, String estimatedTime, boolean tollRequired,
            String roadType) throws SQLException {
        String query = "INSERT INTO routes (origin, destination, distance_km, estimated_time, toll_required, road_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            pstmt.setFloat(3, distanceKm);
            pstmt.setString(4, estimatedTime);
            pstmt.setBoolean(5, tollRequired);
            pstmt.setString(6, roadType);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating route failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating route failed, no ID obtained.");
                }
            }
        }
    }

    public void updateRoute(int routeId, String origin, String destination, float distanceKm, String estimatedTime,
            boolean tollRequired, String roadType) throws SQLException {
        String query = "UPDATE routes SET origin = ?, destination = ?, distance_km = ?, estimated_time = ?, toll_required = ?, road_type = ? WHERE route_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            pstmt.setFloat(3, distanceKm);
            pstmt.setString(4, estimatedTime);
            pstmt.setBoolean(5, tollRequired);
            pstmt.setString(6, roadType);
            pstmt.setInt(7, routeId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating route failed, no rows affected.");
            }
        }
    }

    public void deleteRoute(int routeId) throws SQLException {
        String query = "DELETE FROM routes WHERE route_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting route failed, no rows affected.");
            }
        }
    }

    public List<Route> getAllRoutes() throws SQLException {
        List<Route> routes = new ArrayList<>();
        String query = "SELECT * FROM routes";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Route route = new Route(
                        rs.getInt("route_id"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getFloat("distance_km"),
                        rs.getString("estimated_time"),
                        rs.getBoolean("toll_required"),
                        rs.getString("road_type"));
                routes.add(route);
            }
        }
        return routes;
    }

    public Route getRouteById(int routeId) throws SQLException {
        String query = "SELECT * FROM routes WHERE route_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, routeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Route(
                            rs.getInt("route_id"),
                            rs.getString("origin"),
                            rs.getString("destination"),
                            rs.getFloat("distance_km"),
                            rs.getString("estimated_time"),
                            rs.getBoolean("toll_required"),
                            rs.getString("road_type"));
                }
            }
        }
        return null;
    }
}