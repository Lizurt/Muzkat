package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthorApi {
    @GET("/author/get-all")
    Call<List<GenreEntity>> getAllAuthors();

    @POST("/author/save")
    Call<GenreEntity> saveAuthor(@Body AuthorEntity authorEntity);
}
