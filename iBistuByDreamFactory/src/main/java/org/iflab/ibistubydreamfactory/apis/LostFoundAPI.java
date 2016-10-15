package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.SetIsFoundRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 失物招领接口
 */
public interface LostFoundAPI {
    /**
     * 获得失物招领列表，filter决定是显示所有数据还是只显示用户的数据
     * page (列表页-1)的十倍，如要获取第二页的数据，那么page的值应该为10
     */
    @GET("ibistu/_table/module_lost_found?limit=10&order=createTime%20desc")
    Call<Resource<LostFound>> getLostFound(@Query("offset") String page, @Query("filter") String isFound);

    /**
     * 多图片上传
     *
     * @param uploadFileRequestBody 要上传的文件请求体，可以多文件上传
     */
    @POST("files/ibistu/lost_found/image/")
    Call<UploadResult> uploadFile(@Body UploadFileRequestBody uploadFileRequestBody);

    /**
     * 发布新的lostfound
     */
    @POST("ibistu/_table/module_lost_found")
    Call<ResponseBody> postNewLostFound(@Body LostFound[] postLostFoundRequestBody);

    /**
     * 将某条招领信息设为完结
     * @param id
     * @param setIsFoundRequestBody
     * @return
     */
    @PATCH("ibistu/_table/module_lost_found/{id}")
    Call<ResponseBody> setIsFound(@Path("id") int id, @Body SetIsFoundRequestBody setIsFoundRequestBody);
}
