import java.sql.Date;

public class Booking {
    private int bookingId;
    private int userId;
    private int vehicleId;
    private Date bookingDate;
    private String status;
    private String remarks;

    public Booking(int bookingId, int userId, int vehicleId, Date bookingDate, String status, String remarks) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.bookingDate = bookingDate;
        this.status = status;
        this.remarks = remarks;
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public int getUserId() {
        return userId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    // Setters
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", userId=" + userId +
                ", vehicleId=" + vehicleId +
                ", bookingDate=" + bookingDate +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}