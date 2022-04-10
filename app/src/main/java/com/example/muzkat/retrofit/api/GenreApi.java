package com.example.muzkat.retrofit.api;

import com.example.muzkat.entities.GenreEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GenreApi {
    @GET("/genre/get-all")
    Call<List<GenreEntity>> getAllGenres();

    @POST("/genre/save")
    Call<GenreEntity> saveGenre(@Body GenreEntity genreEntity);
}
