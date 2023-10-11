package com.example.nutritionproject.Retrofit;


import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Model.FoodModel;
import com.example.nutritionproject.Model.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface
{
    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<UserModel> register(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("getUser.php")
    Call<UserModel> getUser(@Field("email") String email);

    @FormUrlEncoded
    @POST("setGoals.php")
    Call<UserModel> setGoals(@Field("userid") int userId, @Field("calorie") int calorie, @Field("protein") int protein, @Field("carb") int carb, @Field("fat") int fat);

    @FormUrlEncoded
    @POST("setPassword.php")
    Call<UserModel> setPassword(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("addFoodItem.php")
    Call<FoodModel> addFoodItem(@Field("upcid") String upcId, @Field("name") String name, @Field("tags") String tags, @Field("dateadded") String dateAdded, @Field("common") Boolean common,
                                @Field("companyname") String companyName, @Field("nutrition") String nutrition);

    @FormUrlEncoded
    @POST("searchFoodItem.php")
    Call<List<FoodModel>> searchFoodItem(@Field("upcid") String upcId, @Field("name") String name);

    @FormUrlEncoded
    @POST("updateUserNutrition.php")
    Call<UserModel> updateNutrition(@Field("userid") int userId, @Field("currentcalories") int calorie, @Field("currentprotein") int protein, @Field("currentcarbs") int carb, @Field("currentfats") int fat, @Field("meals") String meals, @Field("date") String date);

}
