package com.example.nutritionproject.Custom.java.FoodModel;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;

import java.util.HashMap;

public class FoodNutrition {
    public double calories;
    public double servingSize;
    public String servingMeasurement;
    public HashMap<Nutrient, Pair<Double, NutrientMeasurement>> nutrients;

    public FoodNutrition(double calories, double servingSize, String servingMeasurement, HashMap<Nutrient, Pair<Double, NutrientMeasurement>> nutrients) {
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
            mapString += (", " + nutrient.name() + " : " + nutrients.get(nutrient).first + nutrients.get(nutrient).second);
        }

        mapString = mapString.substring(2);

        return String.format("Nutrition : { Calorie : %s, ServingSize : %s %s, Nutrients : { %s } }", calories, servingSize, servingMeasurement, mapString);
    }
}
