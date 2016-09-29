package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.VRMedia;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 *
 */

public interface VRMediaAPI {
    /**
     * 获得VR信息
     */
    @GET("ibistu/_table/module_vr")
    Call<Resource<VRMedia>> getVRMedia();
}
