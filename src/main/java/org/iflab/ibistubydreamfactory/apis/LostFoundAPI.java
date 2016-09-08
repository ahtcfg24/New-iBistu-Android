package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 失物招领接口
 */
public interface LostFoundAPI {
    /**
     * 获得失物招领列表
     */
    @GET("ibistu/_table/module_lost_found")
    Call<Resource<LostFound>> getLostFound();

}
