package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.MapLocation;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 地图接口
 */
public interface MapAPI {
    /**
     * 获得地图地点信息
     */
    @GET("ibistu/_table/module_map")
    Call<Resource<MapLocation>> getMapLocation();
}
