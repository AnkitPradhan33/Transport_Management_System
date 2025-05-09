import java.sql.Date;

public class Driver {
    private int driverId;
    private String name;
    private String licenseNumber;
    private String phone;
    private String address;
    private Date dob;
    private int userId;

    public Driver(int driverId, String name, String licenseNumber, String phone, String address, Date dob, int userId) {
        this.driverId = driverId;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.userId = userId;
    }

    // Getters
    public int getDriverId() {
        return driverId;
    }

    public String getName() {
        return name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Date getDob() {
        return dob;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", name='" + name + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", dob=" + dob +
                ", userId=" + userId +
                '}';
    }
}