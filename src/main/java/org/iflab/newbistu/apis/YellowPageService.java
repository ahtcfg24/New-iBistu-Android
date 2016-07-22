package org.iflab.newbistu.apis;

import org.iflab.newbistu.models.Resource;
import org.iflab.newbistu.models.YellowPageDepart;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 */
public interface YellowPageService {
    /**
     * 获取黄页部门列表信息
     */
    @GET("ibistu/_table/ic_yellowpage?group=depart")
    Call<Resource<YellowPageDepart>> getYellowPageDepart();

    /**
     * 获取黄页部门详情
     * @param filterDepart 要传入的过滤字段：depart={部门字段的值}
     * 相当于:ibistu/_table/ic_yellowpage?offset=1&filter=depart=部门字段的值
     * @return
     */
    @GET("ibistu/_table/ic_yellowpage?offset=1")
    Call<Resource<YellowPageDepart>> getYellowPageDepartDetails(@Query("filter") String filterDepart);
}
