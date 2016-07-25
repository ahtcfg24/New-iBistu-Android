package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.Introduction;
import org.iflab.ibistubydreamfactory.models.Resource;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 */
public interface AboutService {
    /**
     * 获得关于界面中的介绍信息
     * @return
     */
    @GET("ibistu/_table/intro")
    Call<Resource<Introduction>> getAboutIntroDuctions();

}
