import java.sql.Date;

public class Vehicle {
    private int vehicleId;
    private String plateNumber;
    private String model;
    private String type;
    private int capacity;
    private Date purchaseDate;
    private Date insuranceExpiry;
    private String status;

    public Vehicle(int vehicleId, String plateNumber, String model, String type, int capacity, Date purchaseDate,
            Date insuranceExpiry, String status) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.model = model;
        this.type = type;
        this.capacity = capacity;
        this.purchaseDate = purchaseDate;
        this.insuranceExpiry = insuranceExpiry;
        this.status = status;
    }

    // Getters
    public int getVehicleId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getModel() {
        return model;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public Date getInsuranceExpiry() {
        return insuranceExpiry;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setInsuranceExpiry(Date insuranceExpiry) {
        this.insuranceExpiry = insuranceExpiry;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", plateNumber='" + plateNumber + '\'' +
                ", model='" + model + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", purchaseDate=" + purchaseDate +
                ", insuranceExpiry=" + insuranceExpiry +
                ", status='" + status + '\'' +
                '}';
    }
}