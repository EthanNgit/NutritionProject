package com.example.nutritionproject.Custom.java.UserModel;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class UserProfileAdapter extends TypeAdapter<UserProfile>
{

    @Override
    public void write(JsonWriter writer, UserProfile value) throws IOException
    {
        writer.beginObject();

        writer.name("email").value(value.email);
        writer.name("id").value(value.id);

        UserMacros goals = value.goals;
        if (goals != null)
        {
            writer.name("calorie").value(goals.calories);
            writer.name("protein").value(goals.proteins);
            writer.name("carb").value(goals.carbs);
            writer.name("fat").value(goals.fats);
        }

        writer.endObject();
    }

    @Override
    public UserProfile read(JsonReader in) throws IOException
    {
        return null;
    }

}