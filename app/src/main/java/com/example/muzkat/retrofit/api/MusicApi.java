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
    /**
     * Creates a call that can be executed to send a post request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param amount
     * @return
     */
    @POST("/music/get-random")
    Call<List<MusicEntity>> getRandom(@Body Integer amount);

    /**
     * Creates a call that can be executed to send a post request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param getMatchingMusicRequest
     * @return
     */
    @POST("/music/get-matching")
    Call<List<MusicEntity>> getMatching(@Body GetMatchingMusicRequest getMatchingMusicRequest);

    /**
     * Creates a call that can be executed to send a put request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param musicEntity
     * @return
     */
    @PUT("/music/save")
    Call<Boolean> saveMusic(@Body MusicEntity musicEntity);

    /**
     * Creates a call that can be executed to send a put request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param addMusicRequest
     * @return
     */
    @PUT("/music/save-using-names")
    Call<Boolean> saveMusic(@Body AddMusicRequest addMusicRequest);
}
