package com.example.nutritionproject.Custom.java.HistoryModel;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;

import java.util.ArrayList;

public class HistoryProfile
{
    double calorie;
    double protein;
    double carb;
    double fat;
    double alcohol;
    double endCalorie;
    double endProtein;
    double endCarb;
    double endFat;
    double endAlcohol;
    ArrayList<MealProfile> meals;
    String date;
    int streak;

    public HistoryProfile(double calorie, double protein, double carb, double fat, double alcohol, double endCalorie, double endProtein,
                          double endCarb, double endFat, double endAlcohol, ArrayList<MealProfile> meals, String date, int streak)
    {
        this.calorie = calorie;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
        this.alcohol = alcohol;
        this.endCalorie = endCalorie;
        this.endProtein = endProtein;
        this.endCarb = endCarb;
        this.endFat = endFat;
        this.meals = meals;
        this.date = date;
        this.endAlcohol = endAlcohol;
        this.streak = streak;
    }

    @NonNull
    @Override
    public String toString()
    {
        return String.format("History Profile { %s { Calorie goal %s, Protein goal %s, Carb goal %s, Fat goal %s, " +
                        "Alcohol goal %s, End calorie %s, End protein %s, End carb %s, End fat %s, End alcohol %s, " +
                        "Streak %s, Meals { %s }}} ",
                date, calorie, protein, carb, fat, alcohol, endCalorie, endProtein, endCarb, endFat, endAlcohol, streak, meals);
    }

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

    public ArrayList<MealProfile> getMeals()
    {
        return meals;
    }

    public void setMeals(ArrayList<MealProfile> meals)
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

    public int getStreak()
    {
        return streak;
    }

    public void setStreak(int streak)
    {
        this.streak = streak;
    }
}
