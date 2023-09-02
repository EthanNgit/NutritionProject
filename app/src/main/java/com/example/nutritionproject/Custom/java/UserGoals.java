package com.example.nutritionproject.Custom.java;

import androidx.annotation.NonNull;

public class UserGoals {
    public int calorieGoal;
    public int proteinGoal;
    public int carbGoal;
    public int fatGoal;

    public UserGoals(int calGoal, int pGoal, int cGoal, int fGoal) {
        this.calorieGoal = calGoal;
        this.proteinGoal = pGoal;
        this.carbGoal = cGoal;
        this.fatGoal = fGoal;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("{ calorie : %s, protein : %s, carb: %s, fat : %s}", calorieGoal, proteinGoal, carbGoal, fatGoal);
    }
}
