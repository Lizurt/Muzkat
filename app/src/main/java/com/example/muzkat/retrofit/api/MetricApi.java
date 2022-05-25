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
    @GET("/metric/count/{name}")
    Call<Void> getCount(@Path("name") String metricName);

    @POST("/metric/count")
    Call<Void> countUser(@Body CountInMetricRequest countInMetricRequest);

    @PUT("/metric/inc")
    Call<Void> increaseMetric(@Body IncreaseMetricRequest increaseMetricRequest);
}
