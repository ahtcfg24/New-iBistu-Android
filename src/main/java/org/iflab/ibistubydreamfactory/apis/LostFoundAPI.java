package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 失物招领接口
 */
public interface LostFoundAPI {
    /**
     * 获得失物招领列表
     * page (列表页-1)的十倍，如要获取第二页的数据，那么page的值应该为10
     */
    @GET("ibistu/_table/module_lost_found?filter=isFound%3Dtrue&limit=10&order=createTime%20desc")
    Call<Resource<LostFound>> getLostFound(@Query("offset") String page);

}
