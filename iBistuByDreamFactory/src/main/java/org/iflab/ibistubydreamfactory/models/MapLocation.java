package org.iflab.ibistubydreamfactory.models;

/**
 * 地图上的定位地点信息
 */
public class MapLocation extends BaseRecord {

    private String id;
    private String areaName;
    private String areaAddress;
    private String zipCode;
    private double longitude;
    private double latitude;
    private int zoom;

    public MapLocation() {
    }

    public MapLocation(String id, String areaName, String areaAddress, String zipCode, double longitude, double latitude, int zoom) {
        this.id = id;
        this.areaName = areaName;
        this.areaAddress = areaAddress;
        this.zipCode = zipCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.zoom = zoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaAddress() {
        return areaAddress;
    }

    public void setAreaAddress(String areaAddress) {
        this.areaAddress = areaAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "MapLocation{" +
                "id='" + id + '\'' +
                ", areaName='" + areaName + '\'' +
                ", areaAddress='" + areaAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", zoom=" + zoom +
                '}';
    }
}
