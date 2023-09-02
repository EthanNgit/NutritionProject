package com.example.nutritionproject.Custom.java.FoodModel;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.Enums.Nutrient;

import java.util.HashMap;

public class FoodNutrition {
    public double calories;
    public HashMap<Nutrient, Double> nutrients;

    public FoodNutrition(double calories, HashMap<Nutrient, Double> nutrients) {
        this.calories = calories;
        this.nutrients = nutrients;
    }

    @NonNull
    @Override
    public String toString() {
        String mapString = "";

        for (Nutrient nutrient : nutrients.keySet()) {
            mapString += (", " + nutrient.name() + " : " + nutrients.get(nutrient));
        }

        return String.format("Nutrition : { Calorie : %s, %s}", calories, mapString);
    }
}
