package com.example.nutritionproject.Custom;

import static com.example.nutritionproject.Custom.CustomUIMethods.getRandomLightColorHex;

import android.graphics.Color;

import androidx.annotation.NonNull;

public class UserProfile {
    public int id;
    public String email;
    public UserGoals goals;

    public String userColor = getRandomLightColorHex();

    @NonNull
    @Override
    public String toString() {
        return String.format("{ id : %s, email : %s, goals : %s}", id, email, goals.toString());
    }
}
