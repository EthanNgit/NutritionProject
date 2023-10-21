package com.example.nutritionproject.Custom.java.Enums;

public enum ActivityResultCodes
{
    FoodItemView(7488),
    SearchView(2807),
    StatsEditWidget(2994);

    private int code;
    private ActivityResultCodes(int code)
    {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
