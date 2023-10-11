package com.example.nutritionproject.Custom.java.UserModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserGoalsAdapter extends TypeAdapter<UserMacros>
{

    @Override
    public void write(JsonWriter writer, UserMacros value) throws IOException
    {
        writer.beginObject();

        writer.name("calorie").value(value.calories);
        writer.name("protein").value(value.proteins);
        writer.name("carb").value(value.carbs);
        writer.name("fat").value(value.fats);

        writer.endObject();
    }

    @Override
    public UserMacros read(JsonReader in) throws IOException
    {
        return null;
    }

}