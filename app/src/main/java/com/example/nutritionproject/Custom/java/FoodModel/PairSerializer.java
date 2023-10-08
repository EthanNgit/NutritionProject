package com.example.nutritionproject.Custom.java.FoodModel;

import android.util.Pair;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

class PairSerializer extends TypeAdapter<Pair> {


    @Override
    public void write(JsonWriter writer, Pair value) throws IOException {
        writer.beginObject();

        writer.name("left").value((Double) value.first);
        writer.name("right").value((String) value.second);

        writer.endObject();
    }

    @Override
    public Pair read(JsonReader in) throws IOException {
        return null;
    }
}