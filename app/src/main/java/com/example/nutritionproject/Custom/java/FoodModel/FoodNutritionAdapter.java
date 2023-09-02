package com.example.nutritionproject.Custom.java.FoodModel;

import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class FoodNutritionAdapter extends TypeAdapter<FoodNutrition> {
    @Override
    public void write(JsonWriter writer, FoodNutrition value) throws IOException {
        writer.beginObject();

        writer.name("calories").value(value.calories);

        HashMap<Nutrient, Double> nutrients = value.nutrients;

        if (nutrients != null) {
            for (Nutrient nutrient : nutrients.keySet()) {
                writer.name(nutrient.name()).value(nutrients.get(nutrient));
            }
        }

        writer.endObject();
    }

    @Override
    public FoodNutrition read(JsonReader in) throws IOException {
        return null;
    }
}
