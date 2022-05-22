package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.MusicEntity;
import com.example.muzkat.model.entity.UserEntity;
import com.example.muzkat.model.request.AddMusicRequest;
import com.example.muzkat.model.request.GetMatchingMusicRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MusicApi {
    @POST("/music/get-random")
    Call<List<MusicEntity>> getRandom(@Body Integer amount);

    @POST("/music/get-matching")
    Call<List<MusicEntity>> getMatching(@Body GetMatchingMusicRequest getMatchingMusicRequest);

    @PUT("/music/save")
    Call<Boolean> saveMusic(@Body MusicEntity musicEntity);

    @PUT("/music/save-using-names")
    Call<Boolean> saveMusic(@Body AddMusicRequest addMusicRequest);
}
