package org.iflab.ibistubydreamfactory.models;

/**
 *
 */
public class LostFound extends BaseRecord {


    private int id;
    private String title;
    private String details;
    private String createTime;
    private String author;
    private String phone;
    private boolean isFound;
    private String imgUrlList;

    public String getImgUrlList() {
        return imgUrlList;
    }

    public void setImgUrlList(String imgUrlList) {
        this.imgUrlList = imgUrlList;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isIsFound() {
        return isFound;
    }

    public void setIsFound(boolean isFound) {
        this.isFound = isFound;
    }

}
