package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.UpdateInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 检查更新接口
 */
public interface UpdateAPI {
    /**
     * 获得更新信息
     */
    @GET("ibistu/_table/module_update/1")
    Call<UpdateInfo> getUpdateInfo();

}
