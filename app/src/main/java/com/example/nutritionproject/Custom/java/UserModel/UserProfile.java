package com.example.nutritionproject.Custom.java.UserModel;


import androidx.annotation.NonNull;

public class UserProfile {
    public int id;
    public String email;
    public UserGoals goals;

    public float[] userColorHex;

    @NonNull
    @Override
    public String toString() {
        return String.format("{ id : %s, email : %s, goals : %s}", id, email, goals.toString());
    }
}
