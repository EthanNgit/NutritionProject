package com.example.nutritionproject.Custom.java.FoodModel;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealProfile {
    // If you want to store other data about a meal
    // such as date, then you could do it here, for simplicity, i will not

    public String mealName;
    public String timeAdded;
    public ArrayList<FoodProfile> mealComposition;

    public double totalCalories = 0.0;
    public double totalProtein = 0.0;
    public double totalCarbs = 0.0;
    public double totalFats = 0.0;

    public MealProfile(String mealName, ArrayList<FoodProfile> mealComposition) {
        this.mealName = mealName;
        this.mealComposition = mealComposition;
        this.timeAdded = CustomConversionMethods.getHourMinuteSecond();

        mealComposition.forEach((foodProfile -> {
            this.totalCalories += foodProfile.nutrition.nutrients.getOrDefault(Nutrient.Calorie, new Pair<>(0.0, NutrientMeasurement.none)).first;
            this.totalProtein += foodProfile.nutrition.nutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.g)).first;
            this.totalCarbs += foodProfile.nutrition.nutrients.getOrDefault(Nutrient.TotalCarb, new Pair<>(0.0, NutrientMeasurement.g)).first;
            this.totalFats += foodProfile.nutrition.nutrients.getOrDefault(Nutrient.TotalFat, new Pair<>(0.0, NutrientMeasurement.g)).first;
        }));

    }

    @NonNull
    @Override
    public String toString() {
        String profileString = "";

        for (FoodProfile profile: mealComposition) {
            profileString += (", " + profile.toString());
        }

        profileString = profileString.substring(2);

        return String.format("Meal Profile { %s { %s }, TimeAdded %s , TotalCalories %s, TotalProtein %s, TotalCarbs %s, TotalFats %s} ",
                mealName, profileString, timeAdded, totalCalories, totalProtein, totalCarbs, totalFats);

    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if (this == null) return false;
        if (obj == null) return false;

        MealProfile otherMeal = (MealProfile) obj;

        if (!mealName.equals(otherMeal.mealName)) return false;

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> nutrients;
        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> otherNutrients;

        for (int i = 0; i < mealComposition.size(); i++)
        {
            if (otherMeal.mealComposition.get(i) == null) return false;

            nutrients = mealComposition.get(i).nutrition.nutrients;
            otherNutrients = otherMeal.mealComposition.get(i).nutrition.nutrients;

            if (!nutrients.equals(otherNutrients))
            {
                return false;
            }
        }

        return true;
    }
}
