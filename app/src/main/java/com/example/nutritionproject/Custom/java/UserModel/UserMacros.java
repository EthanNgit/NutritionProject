package com.example.nutritionproject.Custom.java.UserModel;

import androidx.annotation.NonNull;

public class UserMacros
{
    public int calories;
    public int proteins;
    public int carbs;
    public int fats;
    public int alcohol;

    public UserMacros()
    {
        this.calories = 0;
        this.proteins = 0;
        this.carbs = 0;
        this.fats = 0;
        this.alcohol = 0;
    }

    public UserMacros(int cal, int prtn, int carb, int fat, int alcohol)
    {
        this.calories = cal;
        this.proteins = prtn;
        this.carbs = carb;
        this.fats = fat;
        this.alcohol = alcohol;
    }

    @NonNull
    @Override
    public String toString()
    {
        String returnString = String.format("{ Calorie : %s, Protein : %s, Carb: %s, Fat : %s, Alcohol : %s}", calories, proteins, carbs, fats, alcohol);

        return returnString;
    }
}
