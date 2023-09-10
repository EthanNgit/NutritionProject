package com.example.nutritionproject.Custom.java.UserModel;

import androidx.annotation.NonNull;

public class UserGoals {
    public int calorieGoal;
    public int proteinGoal;
    public int carbGoal;
    public int fatGoal;

    public UserGoals() {
        this.calorieGoal = 0;
        this.proteinGoal = 0;
        this.carbGoal = 0;
        this.fatGoal = 0;
    }

    public UserGoals(int calGoal, int pGoal, int cGoal, int fGoal) {
        this.calorieGoal = calGoal;
        this.proteinGoal = pGoal;
        this.carbGoal = cGoal;
        this.fatGoal = fGoal;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("{ Calorie : %s, Protein : %s, Carb: %s, Fat : %s}", calorieGoal, proteinGoal, carbGoal, fatGoal);
    }
}
