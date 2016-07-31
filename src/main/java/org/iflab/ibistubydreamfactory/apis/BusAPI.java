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
     * 获取班车列表
     */
    @GET("ibistu/_table/module_bus")
    Call<Resource<Bus>> getBus();
}
