package org.iflab.newbistu.models;

public class YellowPageDepart extends BaseRecord {

    private String id;
    private String name;
    private Object telnum;
    private String depart;

    public YellowPageDepart() {
    }

    public YellowPageDepart(String id, String name, Object telnum, String depart) {
        this.id = id;
        this.name = name;
        this.telnum = telnum;
        this.depart = depart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getTelnum() {
        return telnum;
    }

    public void setTelnum(Object telnum) {
        this.telnum = telnum;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    @Override
    public String toString() {
        return "YellowPageDepart{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", telnum=" + telnum +
                ", depart='" + depart + '\'' +
                '}';
    }
}