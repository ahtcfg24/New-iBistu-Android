package org.iflab.ibistubydreamfactory.models;

/**
 * 关于里的介绍模型
 */
public class Introduction extends BaseRecord{
    private Integer id;
    private String mod;
    private String introName;
    private String introCont;

    public Introduction() {
    }

    public Introduction(Integer id, String mod, String introName, String introCont) {
        this.id = id;
        this.mod = mod;
        this.introName = introName;
        this.introCont = introCont;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getIntroName() {
        return introName;
    }

    public void setIntroName(String introName) {
        this.introName = introName;
    }

    public String getIntroCont() {
        return introCont;
    }

    public void setIntroCont(String introCont) {
        this.introCont = introCont;
    }

    @Override
    public String toString() {
        return "Introduction{" +
                "id=" + id +
                ", mod='" + mod + '\'' +
                ", introName='" + introName + '\'' +
                ", introCont='" + introCont + '\'' +
                '}';
    }
}
