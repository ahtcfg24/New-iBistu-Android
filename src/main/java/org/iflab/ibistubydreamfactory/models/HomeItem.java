package org.iflab.ibistubydreamfactory.models;

/**
 * 描述主页每个模块数据的类，用于与GridView适配
 *
 * @date 2015/8/23
 * @time 20:04
 */
public class HomeItem {
    private int iconId;
    private String itemName;
    private Class itemModule;

    public HomeItem(int iconId, String itemName, Class itemModule) {

        this.iconId = iconId;
        this.itemName = itemName;
        this.itemModule = itemModule;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Class getItemModule() {
        return itemModule;
    }

    public void setItemModule(Class itemModule) {
        this.itemModule = itemModule;
    }
}
