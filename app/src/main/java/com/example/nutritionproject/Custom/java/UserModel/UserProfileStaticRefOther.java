package com.example.nutritionproject.Custom.java.UserModel;

import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.Utility.Event;

import java.util.ArrayList;
import java.util.List;

public class UserProfileStaticRefOther {
    public static ArrayList<MealProfile> userMealHistory = new ArrayList<>();

    public static Event onUserMealHistoryUpdate = new Event();
}
