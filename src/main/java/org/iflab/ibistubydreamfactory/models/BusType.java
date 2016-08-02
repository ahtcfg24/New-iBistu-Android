package org.iflab.ibistubydreamfactory.models;

import java.util.ArrayList;
import java.util.List;

/**
 * 班车类型：教学班车或者通勤班车
 */
public class BusType {
    private String busTypeName;//类型名
    private List<Bus> busItemList = new ArrayList<>();//该类下的班车数据

    public BusType(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void addItem(Bus bus) {
        busItemList.add(bus);
    }

    /**
     * 获取Item内容
     */
    public Bus getItem(int position) {
        return busItemList.get(position - 1);
    }

    /**
     * 当前类别Item总数。Category也需要占用一个Item
     */
    public int getItemCount() {
        return busItemList.size() + 1;
    }

    public List<Bus> getBusItemList() {
        return busItemList;
    }
}
