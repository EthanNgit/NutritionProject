package com.example.nutritionproject.Model;

import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HistoryModel
{
    @Expose
    @SerializedName("calorie")
    private double calorie;
    @Expose
    @SerializedName("protein")
    private double protein;
    @Expose
    @SerializedName("carb")
    private double carb;
    @Expose
    @SerializedName("fat")
    private double fat;
    @Expose
    @SerializedName("alcohol")
    private double alcohol;
    @Expose
    @SerializedName("endCalorie")
    private double endCalorie;
    @Expose
    @SerializedName("endProtein")
    private double endProtein;
    @Expose
    @SerializedName("endCarb")
    private double endCarb;
    @Expose
    @SerializedName("endFat")
    private double endFat;
    @Expose
    @SerializedName("endAlcohol")
    private double endAlcohol;
    @Expose
    @SerializedName("meals")
    private String meals;
    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("streak")
    private int steak;

    public double getCalorie()
    {
        return calorie;
    }

    public void setCalorie(double calorie)
    {
        this.calorie = calorie;
    }

    public double getProtein()
    {
        return protein;
    }

    public void setProtein(double protein)
    {
        this.protein = protein;
    }

    public double getCarb()
    {
        return carb;
    }

    public void setCarb(double carb)
    {
        this.carb = carb;
    }

    public double getFat()
    {
        return fat;
    }

    public void setFat(double fat)
    {
        this.fat = fat;
    }

    public double getAlcohol()
    {
        return alcohol;
    }

    public void setAlcohol(double alcohol)
    {
        this.alcohol = alcohol;
    }

    public double getEndCalorie()
    {
        return endCalorie;
    }

    public void setEndCalorie(double endCalorie)
    {
        this.endCalorie = endCalorie;
    }

    public double getEndProtein()
    {
        return endProtein;
    }

    public void setEndProtein(double endProtein)
    {
        this.endProtein = endProtein;
    }

    public double getEndCarb()
    {
        return endCarb;
    }

    public void setEndCarb(double endCarb)
    {
        this.endCarb = endCarb;
    }

    public double getEndFat()
    {
        return endFat;
    }

    public void setEndFat(double endFat)
    {
        this.endFat = endFat;
    }

    public double getEndAlcohol()
    {
        return endAlcohol;
    }

    public void setEndAlcohol(double endAlcohol)
    {
        this.endAlcohol = endAlcohol;
    }

    public String getMeals()
    {
        return meals;
    }

    public void setMeals(String meals)
    {
        this.meals = meals;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
    public int getSteak()
    {
        return steak;
    }

    public void setSteak(int steak)
    {
        this.steak = steak;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Expose
        @SerializedName("success")
        private boolean success;

        @Expose
        @SerializedName("message")
        private String message;
}
