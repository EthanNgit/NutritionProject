package com.example.nutritionproject.Custom.java.FoodModel;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.Enums.Measurement;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;

import java.util.HashMap;

public class FoodNutrition {
    public double calories;
    public double servingSize;
    public String servingMeasurement;
    public HashMap<Nutrient, Double> nutrients;

    public FoodNutrition(double calories, double servingSize, String servingMeasurement, HashMap<Nutrient, Double> nutrients) {
        this.calories = calories;
        this.servingSize = servingSize;
        this.servingMeasurement = servingMeasurement;
        this.nutrients = nutrients;
    }

    @NonNull
    @Override
    public String toString() {
        String mapString = "";

        for (Nutrient nutrient : nutrients.keySet()) {
            mapString += (", " + nutrient.name() + " : " + nutrients.get(nutrient));
        }

        mapString = mapString.substring(2);

        return String.format("Nutrition : { Calorie : %s, ServingSize : %s %s, Nutrients : { %s } }", calories, servingSize, servingMeasurement, mapString);
    }
}
