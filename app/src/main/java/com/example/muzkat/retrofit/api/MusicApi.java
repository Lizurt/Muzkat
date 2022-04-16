package com.example.muzkat.retrofit.api;

import com.example.muzkat.entities.MusicEntity;
import com.example.muzkat.entities.UserEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MusicApi {
    @POST("/music/get-random")
    Call<List<MusicEntity>> getRandom(@Body Integer amount);

    @POST("/music/get-matching")
    Call<List<MusicEntity>> getMatching(@Body Integer amount, @Body UserEntity userEntity);

    @POST("/music/save")
    Call<MusicEntity> saveMusic(@Body MusicEntity musicEntity);
}
