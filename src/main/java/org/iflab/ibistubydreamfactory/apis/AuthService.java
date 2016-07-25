package org.iflab.ibistubydreamfactory.apis;

import org.iflab.ibistubydreamfactory.models.LoginRequest;
import org.iflab.ibistubydreamfactory.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 */
public interface AuthService {
    @POST("system/admin/session")
    Call<User> login(@Body LoginRequest loginRequest);
}
