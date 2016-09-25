package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.YellowPageDepartment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 */
public interface YellowPageAPI {
    /**
     * 获取黄页部门列表
     */
    @GET("ibistu/_table/module_yellowpage?filter=isDisplay%3D1&offset=1&group=department")
    Call<Resource<YellowPageDepartment>> getYellowPageDepart();

    /**
     * 获取黄页某个部门的详细信息
     * @param filterDepart 要传入的过滤字段：depart={部门字段的值}
     * query指的是url后面接问号参数，一个参数及参数值构成一个query
     * 相当于:ibistu/_table/module_yellowpage?offset=1&filter=department=xxx(具体某个部门的值)
     * @return*
     */
    @GET("ibistu/_table/module_yellowpage?offset=1")
    Call<Resource<YellowPageDepartment>> getYellowPageDetails(@Query("filter") String filterDepart);
}
