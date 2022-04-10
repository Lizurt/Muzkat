package com.example.muzkat.retrofit.api;

import com.example.muzkat.entities.UserEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {
    @GET("user/get-all")
    Call<List<UserEntity>> getAllUsers();

    @POST("user/save")
    Call<UserEntity> saveUser(@Body UserEntity userEntity);
}
