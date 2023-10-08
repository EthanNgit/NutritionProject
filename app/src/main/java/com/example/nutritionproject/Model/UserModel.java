package com.example.nutritionproject.Model;

import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("email")
    private String email;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("calorie")
    private int calorie;

    @Expose
    @SerializedName("protein")
    private int protein;

    @Expose
    @SerializedName("carb")
    private int carb;

    @Expose
    @SerializedName("fat")
    private int fat;

    @Expose
    @SerializedName("currentcalories")
    private int currentCalories;

    @Expose
    @SerializedName("currentprotein")
    private int currentProtein;

    @Expose
    @SerializedName("currentcarbs")
    private int currentCarbs;

    @Expose
    @SerializedName("currentfats")
    private int currentFats;

    @Expose
    @SerializedName("meals")
    private Object meals;

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("message")
    private String message;

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getCarb() {
        return carb;
    }

    public void setCarb(int carb) {
        this.carb = carb;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCurrentCalories() {
        return currentCalories;
    }

    public void setCurrentCalories(int currentCalories) {
        this.currentCalories = currentCalories;
    }

    public int getCurrentProtein() {
        return currentProtein;
    }

    public void setCurrentProtein(int currentProtein) {
        this.currentProtein = currentProtein;
    }

    public int getCurrentCarbs() {
        return currentCarbs;
    }

    public void setCurrentCarbs(int currentCarbs) {
        this.currentCarbs = currentCarbs;
    }

    public int getCurrentFats() {
        return currentFats;
    }

    public void setCurrentFats(int currentFats) {
        this.currentFats = currentFats;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getMeals() {
        return meals;
    }

    public void setMeals(Object meals) {
        this.meals = meals;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
