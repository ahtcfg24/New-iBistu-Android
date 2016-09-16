package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.UploadFileRequestBody;
import org.iflab.ibistubydreamfactory.models.UploadSuccessModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */
public interface UploadFilesAPI {
    /**
     * 多文件上传
     *
     * @param uploadFileRequestBody 要上传的文件请求体，可以多文件上传
     */
    @POST("files/ibistu/lost_found/image/")
    Call<UploadSuccessModel> uploadFile(@Body UploadFileRequestBody uploadFileRequestBody);

}

