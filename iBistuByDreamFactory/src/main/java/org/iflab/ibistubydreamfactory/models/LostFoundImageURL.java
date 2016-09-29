package org.iflab.ibistubydreamfactory.models;

/**
 *
 */
public class LostFoundImageURL extends BaseRecord {
    private String url;

    public LostFoundImageURL() {
    }


    public LostFoundImageURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LostFoundImageURL{" +
                "url='" + url + '\'' +
                '}';
    }
}
