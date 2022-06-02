package com.example.muzkat.retrofit.api;

import com.example.muzkat.model.request.CountInMetricRequest;
import com.example.muzkat.model.request.IncreaseMetricRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MetricApi {
    /**
     * Creates a call that can be executed to send a get request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param metricName
     * @return
     */
    @GET("/metric/count/{name}")
    Call<Void> getCount(@Path("name") String metricName);

    /**
     * Creates a call that can be executed to send a post request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param countInMetricRequest
     * @return
     */
    @POST("/metric/count")
    Call<Void> countUser(@Body CountInMetricRequest countInMetricRequest);

    /**
     * Creates a call that can be executed to send a get request to a server with the given params
     * (body). Will return the written result in the server's body
     * @param increaseMetricRequest
     * @return
     */
    @PUT("/metric/inc")
    Call<Void> increaseMetric(@Body IncreaseMetricRequest increaseMetricRequest);
}
