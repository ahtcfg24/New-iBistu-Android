package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.About;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 关于接口
 */
public interface AboutAPI {
    /**
     * 获得关于界面中的介绍信息
     * @return
     */
    @GET("ibistu/_table/module_about")
    Call<Resource<About>> getAboutIntroductions();

}
