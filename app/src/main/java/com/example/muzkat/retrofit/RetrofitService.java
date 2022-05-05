package com.example.muzkat.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        // dunno how i'll be getting metrics and statistics with my cool dynamic ip.
        // i shouldn't buy a server machine, yes? Anyway, local ip for now
        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://muzkat-server.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
