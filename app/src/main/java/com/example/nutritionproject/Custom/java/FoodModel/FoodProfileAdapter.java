package com.example.nutritionproject.Custom.java.FoodModel;

import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.UserModel.UserGoals;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class FoodProfileAdapter extends TypeAdapter<FoodProfile> {
    @Override
    public void write(JsonWriter writer, FoodProfile value) throws IOException {
        writer.beginObject();

        writer.name("upcId").value(value.upcId);
        writer.name("name").value(value.name);

        Set<FoodTag> tags = value.tags;
        if (tags != null) {
            for (FoodTag tag : tags) {
                writer.name(tag.name());
            }
        }

        writer.name("dateAdded").value(String.valueOf(value.dateAdded));
        writer.name("isCommon").value(value.isCommon);
        writer.name("brandName").value(value.brandName);
        writer.name("isVerified").value(value.isVerified);

        FoodNutrition nutrition = value.nutrition;
        if (nutrition != null) {
            writer.name("calories").value(nutrition.calories);

            HashMap<Nutrient, Double> nutrients = nutrition.nutrients;

            if (nutrients != null) {
                for (Nutrient nutrient : nutrients.keySet()) {
                    writer.name(nutrient.name()).value(nutrients.get(nutrient));
                }
            }

        }

        writer.endObject();
    }

    @Override
    public FoodProfile read(JsonReader in) throws IOException {
        return null;
    }
}