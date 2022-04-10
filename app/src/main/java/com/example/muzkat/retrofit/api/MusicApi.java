package com.example.muzkat.retrofit.api;

import com.example.muzkat.entities.MusicEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MusicApi {
    @GET("/music/get-all")
    Call<List<MusicEntity>> getAllMusic();

    @POST("/music/save")
    Call<MusicEntity> saveMusic(@Body MusicEntity musicEntity);
}
