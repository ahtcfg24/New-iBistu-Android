package org.iflab.ibistubydreamfactory.models;

/**
 * 班车
 */
public class Bus extends BaseRecord{


    private int id;
    private String busName;
    private String busType;
    private String departureTime;
    private String returnTime;
    private String busLine;
    private String busIntro;

    public Bus() {
    }

    public Bus(int id, String busName, String busType, String departureTime, String returnTime, String busLine, String busIntro) {
        this.id = id;
        this.busName = busName;
        this.busType = busType;
        this.departureTime = departureTime;
        this.returnTime = returnTime;
        this.busLine = busLine;
        this.busIntro = busIntro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getBusLine() {
        return busLine;
    }

    public void setBusLine(String busLine) {
        this.busLine = busLine;
    }

    public String getBusIntro() {
        return busIntro;
    }

    public void setBusIntro(String busIntro) {
        this.busIntro = busIntro;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", busName='" + busName + '\'' +
                ", busType='" + busType + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", returnTime='" + returnTime + '\'' +
                ", busLine='" + busLine + '\'' +
                ", busIntro='" + busIntro + '\'' +
                '}';
    }
}
