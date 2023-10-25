package com.example.nutritionproject.Custom.java.HistoryModel;

import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class HistoryProfileAdapter extends TypeAdapter<HistoryProfile> {

    @Override
    public void write(JsonWriter writer, HistoryProfile value) throws IOException
    {
        Gson gson = new Gson();

        writer.name("calorie").value(value.calorie);
        writer.name("protein").value(value.protein);
        writer.name("carb").value(value.carb);
        writer.name("fat").value(value.fat);
        writer.name("alcohol").value(value.alcohol);
        writer.name("endCalorie").value(value.endCalorie);
        writer.name("endProtein").value(value.endProtein);
        writer.name("endCarb").value(value.endCarb);
        writer.name("endFat").value(value.endFat);
        writer.name("endAlcohol").value(value.endAlcohol);
        writer.name("meals").value(gson.toJson(value.meals));
        writer.name("date").value(value.date);
        writer.name("streak").value(value.streak);

    }

    @Override
    public HistoryProfile read(JsonReader in) throws IOException
    {
        return null;
    }
}
