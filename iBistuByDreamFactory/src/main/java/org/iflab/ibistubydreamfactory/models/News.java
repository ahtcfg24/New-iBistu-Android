package org.iflab.ibistubydreamfactory.models;

/**
 *
 */
public class News extends BaseRecord {
    private String newsLink;
    private String newsTitle;
    private String newsTime;
    private String newsImage;
    private String newsIntro;

    public News() {
    }

    public News(String newsLink, String newsTitle, String newsTime, String newsImage, String newsIntro) {
        this.newsLink = newsLink;
        this.newsTitle = newsTitle;
        this.newsTime = newsTime;
        this.newsImage = newsImage;
        this.newsIntro = newsIntro;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsIntro() {
        return newsIntro;
    }

    public void setNewsIntro(String newsIntro) {
        this.newsIntro = newsIntro;
    }

    @Override
    public String toString() {
        return "News{" +
                "newsLink='" + newsLink + '\'' +
                ", newsTitle='" + newsTitle + '\'' +
                ", newsTime='" + newsTime + '\'' +
                ", newsImage='" + newsImage + '\'' +
                ", newsIntro='" + newsIntro + '\'' +
                '}';
    }
}
