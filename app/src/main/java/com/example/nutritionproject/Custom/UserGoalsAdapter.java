package com.example.nutritionproject.Custom;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserGoalsAdapter extends TypeAdapter<UserGoals> {

    @Override
    public void write(JsonWriter writer, UserGoals value) throws IOException {
        writer.beginObject();

        writer.name("calorie").value(value.calorieGoal);
        writer.name("protein").value(value.proteinGoal);
        writer.name("carb").value(value.carbGoal);
        writer.name("fat").value(value.fatGoal);

        writer.endObject();
    }

    @Override
    public UserGoals read(JsonReader in) throws IOException {
        return null;
    }

}