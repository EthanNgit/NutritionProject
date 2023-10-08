package com.example.nutritionproject.Custom.java.FoodModel;

import android.util.Pair;

import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;

public class FoodNutritionAdapter extends TypeAdapter<FoodNutrition> {
    @Override
    public void write(JsonWriter writer, FoodNutrition value) throws IOException {
        writer.beginObject();

        writer.name("calories").value(value.calories);
        writer.name("servingSize").value(value.servingSize);
        writer.name("servingMeasurement").value(value.servingMeasurement);

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> nutrients = value.nutrients;

        if (nutrients != null) {
            for (Nutrient nutrient : nutrients.keySet()) {
                writer.name(nutrient.name()).value(nutrients.get(nutrient).first + nutrients.get(nutrient).second.name());
            }
        }

        writer.endObject();
    }

    @Override
    public FoodNutrition read(JsonReader in) throws IOException {
        return null;
    }
}
