package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.Bus;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 */
public interface BusAPI {
    /**
     * 获取通勤班车信息
     */
    @GET("ibistu/_table/module_bus?filter=busType%3D%E9%80%9A%E5%8B%A4%E7%8F%AD%E8%BD%A6")
    Call<Resource<Bus>> getScheduledBus();

    /**
     * 获得教学班车信息
     * @return
     */
    @GET("ibistu/_table/module_bus?filter=busType%3D%E6%95%99%E5%AD%A6%E7%8F%AD%E8%BD%A6")
    Call<Resource<Bus>> getTeachBus();

    /**
     * 获得班车信息
     */
    @GET("ibistu/_table/module_bus")
    Call<Resource<Bus>> getBus();
}
