package com.example.nutritionproject.Retrofit;

import com.example.nutritionproject.Model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<UserModel> register(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("setGoals.php")
    Call<UserModel> setGoals(@Field("userid") int userId, @Field("calorie") int calorie, @Field("protein") int protein, @Field("carb") int carb, @Field("fat") int fat);
}
