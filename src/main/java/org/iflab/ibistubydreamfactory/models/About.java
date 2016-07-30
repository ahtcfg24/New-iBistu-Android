package org.iflab.ibistubydreamfactory.models;

/**
 * 关于模型
 */
public class About extends BaseRecord{

    private int id;
    private String aboutName;
    private String aboutDetails;

    public About() {
    }

    public About(int id, String aboutName, String aboutDetails) {
        this.id = id;
        this.aboutName = aboutName;
        this.aboutDetails = aboutDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAboutName() {
        return aboutName;
    }

    public void setAboutName(String aboutName) {
        this.aboutName = aboutName;
    }

    public String getAboutDetails() {
        return aboutDetails;
    }

    public void setAboutDetails(String aboutDetails) {
        this.aboutDetails = aboutDetails;
    }

    @Override
    public String toString() {
        return "About{" +
                "id=" + id +
                ", aboutName='" + aboutName + '\'' +
                ", aboutDetails='" + aboutDetails + '\'' +
                '}';
    }
}
