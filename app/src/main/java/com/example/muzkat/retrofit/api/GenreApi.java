package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.GenreEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GenreApi {
    /**
     * Creates a call that can be executed to send a get request to a server with the given params
     * (body). Will return the written result in the server's body
     * @return
     */
    @GET("/genre/get-all")
    Call<List<GenreEntity>> getAllGenres();

    /**
     * Creates a call that can be executed to send a post request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param genreEntity
     * @return
     */
    @POST("/genre/save")
    Call<GenreEntity> saveGenre(@Body GenreEntity genreEntity);
}
