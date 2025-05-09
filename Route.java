public class Route {
    private int routeId;
    private String origin;
    private String destination;
    private float distanceKm;
    private String estimatedTime;
    private boolean tollRequired;
    private String roadType;

    public Route(int routeId, String origin, String destination, float distanceKm, String estimatedTime,
            boolean tollRequired, String roadType) {
        this.routeId = routeId;
        this.origin = origin;
        this.destination = destination;
        this.distanceKm = distanceKm;
        this.estimatedTime = estimatedTime;
        this.tollRequired = tollRequired;
        this.roadType = roadType;
    }

    // Getters
    public int getRouteId() {
        return routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public float getDistanceKm() {
        return distanceKm;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public boolean isTollRequired() {
        return tollRequired;
    }

    public String getRoadType() {
        return roadType;
    }

    // Setters
    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDistanceKm(float distanceKm) {
        this.distanceKm = distanceKm;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setTollRequired(boolean tollRequired) {
        this.tollRequired = tollRequired;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", distanceKm=" + distanceKm +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", tollRequired=" + tollRequired +
                ", roadType='" + roadType + '\'' +
                '}';
    }
}