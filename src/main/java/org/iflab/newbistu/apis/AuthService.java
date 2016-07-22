package org.iflab.newbistu.apis;

import org.iflab.newbistu.models.LoginRequest;
import org.iflab.newbistu.models.User;

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
