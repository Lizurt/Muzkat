package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.entity.AuthorEntity;
import com.example.muzkat.model.entity.GenreEntity;
import com.example.muzkat.model.entity.UserEntity;
import com.example.muzkat.model.request.AddFavAuthorRequest;
import com.example.muzkat.model.request.AddFavGenreRequest;
import com.example.muzkat.model.request.DeleteFavAuthorRequest;
import com.example.muzkat.model.request.DeleteFavGenreRequest;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("/user/add-fav-author")
    Call<Void> addFavAuthor(@Body AddFavAuthorRequest addFavAuthorRequest);

    @POST("/user/add-fav-genre")
    Call<Void> addFavGenre(@Body AddFavGenreRequest addFavGenreRequest);

    @POST("/user/del-fav-author")
    Call<Void> delFavAuthor(@Body DeleteFavAuthorRequest deleteFavAuthorRequest);

    @POST("/user/del-fav-genre")
    Call<Void> delFavGenre(@Body DeleteFavGenreRequest deleteFavGenreRequest);
}
