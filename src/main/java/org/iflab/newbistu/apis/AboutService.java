package org.iflab.newbistu.apis;

import org.iflab.newbistu.models.Introduction;
import org.iflab.newbistu.models.Resource;

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
