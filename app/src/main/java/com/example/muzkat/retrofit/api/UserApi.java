package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.entity.UserEntity;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("/user/try-login")
    Call<Boolean> tryLogin(@Body UserEntity userEntity);

    @POST("/user/try-logon")
    Call<Boolean> tryLogon(@Body UserEntity userEntity);

    @GET("/user/get-fav-genres/{login}")
    Call<Set<GenreEntity>> getFavGenres(@Path("login") String login);

    @GET("/user/get-fav-authors/{login}")
    Call<Set<AuthorEntity>> getFavAuthors(@Path("login") String login);
}
