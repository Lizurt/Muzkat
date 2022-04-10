package com.example.muzkat.retrofit.api;

import com.example.muzkat.entities.UserEntity;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("user/try-login")
    Call<Boolean> tryLogin(@Body UserEntity userEntity);

    @POST("user/try-logon")
    Call<Boolean> tryLogon(@Body UserEntity userEntity);
}
