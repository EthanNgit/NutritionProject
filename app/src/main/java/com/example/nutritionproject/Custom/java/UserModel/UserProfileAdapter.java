package com.example.nutritionproject.Custom.java.UserModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserProfileAdapter extends TypeAdapter<UserProfile> {

    @Override
    public void write(JsonWriter writer, UserProfile value) throws IOException {
        writer.beginObject();

        writer.name("id").value(value.id);
        writer.name("email").value(value.email);
        UserGoals goals = value.goals;
        if (goals != null) {
            writer.name("calorie").value(goals.calorieGoal);
            writer.name("protein").value(goals.proteinGoal);
            writer.name("carb").value(goals.carbGoal);
            writer.name("fat").value(goals.fatGoal);
        }

        writer.endObject();
    }

    @Override
    public UserProfile read(JsonReader in) throws IOException {
        return null;
    }

}