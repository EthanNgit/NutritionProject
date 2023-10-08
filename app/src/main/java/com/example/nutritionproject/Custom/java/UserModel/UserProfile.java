package com.example.nutritionproject.Custom.java.UserModel;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;

public class UserProfile {
    public int id;
    public String email;
    public UserMacros goals;
    public UserMacros currentMacros;

    public float[] userColorHex;

    public UserProfile() {
        this.id = -1;
        this.email = "";
        this.goals = new UserMacros();
        this.currentMacros = new UserMacros();
        this.userColorHex = CustomUIMethods.generateRandomColorBasedOffBrand();
    }

    public UserProfile(int id, String email, UserMacros goals, UserMacros currentMacros , @Nullable float[] userColorHex) {
        this.id = id;
        this.email = email;
        this.goals = goals;
        this.currentMacros = currentMacros;

        if (userColorHex == null) {
            this.userColorHex = CustomUIMethods.generateRandomColorBasedOffBrand();
        } else {
            this.userColorHex = userColorHex;
        }

    }

    @NonNull
    @Override
    public String toString() {
        return String.format("{ id : %s, email : %s, goals : %s, current : %s}", id, email, goals.toString(), currentMacros.toString());
    }
}
