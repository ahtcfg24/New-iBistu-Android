package org.iflab.ibistubydreamfactory.models;

/**
 * 黄页部门
 */
public class YellowPageDepartment extends BaseRecord {

    private int id;
    private String name;
    private String telephone;
    private String department;
    private boolean isDisplay;

    public YellowPageDepartment() {
    }

    public YellowPageDepartment(int id, String name, String telephone, String department, boolean isDisplay) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.department = department;
        this.isDisplay = isDisplay;
    }

    @Override
    public String toString() {
        return "YellowPageDepartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                ", department=" + department +
                ", isDisplay=" + isDisplay +
                '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }
}