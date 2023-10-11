package com.example.nutritionproject.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    //public static final String BASE_URL = "http://10.0.2.2:80/nutrutionprojectdb/";
    public static final String BASE_URL = "URL GOES HERE";

    public static Retrofit retrofit = null;

    public static Retrofit getApiClient()
    {
        if (retrofit != null) return retrofit;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return  retrofit;
    }
}

