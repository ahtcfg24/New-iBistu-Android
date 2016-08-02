package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.Bus;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 班车API
 */
public interface BusAPI {

    /**
     * 获得班车信息
     */
    @GET("ibistu/_table/module_bus")
    Call<Resource<Bus>> getBus();
}
