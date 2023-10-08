package com.example.nutritionproject.Custom.java.FoodModel;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealProfileAdapter extends TypeAdapter<MealProfile> {
    @Override
    public void write(JsonWriter writer, MealProfile value) throws IOException {
        writer.beginObject();

        ArrayList<FoodProfile> mealComposition = value.mealComposition;

        writer.name("Name").value(value.mealName);
        writer.name("timeAdded").value(value.timeAdded);
        writer.name("TotalCalories").value(value.totalCalories);
        writer.name("TotalProtein").value(value.totalProtein);
        writer.name("TotalCarbs").value(value.totalCarbs);
        writer.name("TotalFats").value(value.totalFats);

        Gson gson = new Gson();
        if (mealComposition != null) {
            for (FoodProfile profile: mealComposition) {
                writer.name(profile.name).value(gson.toJson(profile));
            }
        }

    }

    @Override
    public MealProfile read(JsonReader in) throws IOException {
        return null;
    }
}
