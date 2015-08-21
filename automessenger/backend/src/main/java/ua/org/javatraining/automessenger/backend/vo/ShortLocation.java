package ua.org.javatraining.automessenger.backend.vo;

/**
 * Created by fisher on 05.08.15.
 */

public class ShortLocation {
    private String country = "";
    private String adminArea = "";
    private String region = "";

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
