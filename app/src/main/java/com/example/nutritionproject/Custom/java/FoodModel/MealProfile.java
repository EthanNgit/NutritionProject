package com.example.nutritionproject.Custom.java.FoodModel;

import androidx.annotation.NonNull;

import com.example.nutritionproject.Custom.java.Custom.UI.MealListAdapter;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MealProfile {
    public List<FoodProfile> composition;
    public FoodProfile mealProfile;

    private double compCalories = 0;

    public MealProfile(String mealName, List<FoodProfile> composition) {
        this.composition = composition;

        HashMap<Nutrient, Double> compNutrients = new HashMap<>();

        for (FoodProfile profile : composition) {
            // O(n^3) I believe for profile -> for nutrient -> merging. Probably could be done better.
            compCalories += profile.nutrition.calories;

            profile.nutrition.nutrients.forEach((key, value) -> compNutrients.merge(key, value, (oldValue, newValue) -> oldValue + newValue));
        }

        FoodNutrition compNutrition = new FoodNutrition(compCalories, 1, "Serving", compNutrients);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        mealProfile = new FoodProfile(null, mealName, null, formatter.format(date), false, "Recipe", false, compNutrition);

    }

    @NonNull
    @Override
    public String toString() {
        //TODO: maybe change this
        String body = "";
        for (FoodProfile profile : composition) {
            body += profile.toString() + ", ";
        }

        body = body.substring(0, body.length() - 3);

        return String.format("MealProfile : { %s }", body);
    }
}
