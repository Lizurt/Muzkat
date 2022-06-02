package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthorApi {
    /**
     * Creates a call that can be executed to send a get request to a server with the given params
     * (body). Will return the written result in the server's body
     * @return
     */
    @GET("/author/get-all")
    Call<List<GenreEntity>> getAllAuthors();

    /**
     * Creates a call that can be executed to send a post request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param authorEntity
     * @return
     */
    @POST("/author/save")
    Call<GenreEntity> saveAuthor(@Body AuthorEntity authorEntity);
}
