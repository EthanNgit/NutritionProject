package com.example.nutritionproject.Custom;

public class CustomConversionMethods {
    private static final double LBS_TO_KG = 2.205;
    private static final int FT_TO_IN = 12;
    private static final double IN_TO_CM = 2.54;

    public static double getImperialHeight(int ft, int in) {
        int totalIn = ftToIn(ft) + in;

        return inToCm(totalIn);
    }

    public static double getImperialWeight(double lbs) {
        return lbs / LBS_TO_KG;
    }

    private static double inToCm(int in) {
        return in * IN_TO_CM;
    }

    private static int ftToIn(int ft) {
        return ft * FT_TO_IN;
    }
}
