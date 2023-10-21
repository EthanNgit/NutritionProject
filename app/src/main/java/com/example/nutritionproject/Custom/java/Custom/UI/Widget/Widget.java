package com.example.nutritionproject.Custom.java.Custom.UI.Widget;

public enum Widget
{
    Calorie(1),
    Macros(2),
    Weight(3),
    Goals(4),
    Streaks(5),
    Nutrition(6);

    private int id;
    private Widget(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}
