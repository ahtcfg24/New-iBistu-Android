package org.iflab.ibistubydreamfactory.models;

/**
 * 班车路线中的站点
 */
public class BusStation extends BaseRecord{

    private String station;
    private String arrivalTime;

    public BusStation() {
    }

    public BusStation(String station, String arrivalTime) {
        this.station = station;
        this.arrivalTime = arrivalTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "BusStation{" +
                "station='" + station + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                '}';
    }
}
