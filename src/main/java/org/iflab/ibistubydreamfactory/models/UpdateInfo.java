package org.iflab.ibistubydreamfactory.models;

/**
 * app版本更新信息
 */
public class UpdateInfo extends BaseRecord {

    private int id;
    private String name;
    private String path;
    private String version;
    private int versionCode;
    private String versionInfo;
    private String updateTime;
    private String updateSize;

    public UpdateInfo() {
    }


    public UpdateInfo(int id, String name, String path, String version, int versionCode, String versionInfo, String updateTime, String updateSize) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.version = version;
        this.versionCode = versionCode;
        this.versionInfo = versionInfo;
        this.updateTime = updateTime;
        this.updateSize = updateSize;
    }

    public String getUpdateSize() {
        return updateSize;
    }

    public void setUpdateSize(String updateSize) {
        this.updateSize = updateSize;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "发现新版本：" + getVersion() + "\n更新内容：" + getVersionInfo() + "\n更新时间：" + getUpdateTime() + "\n安装包大小：" + getUpdateSize() + "M";
    }
}
