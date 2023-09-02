package com.example.nutritionproject.Custom.java.Custom;

public class CustomConversionMethods {
    private static final double LBS_TO_KG = 2.205;
    private static final int FT_TO_IN = 12;
    private static final double IN_TO_CM = 2.54;

    /**
     *
     * @apiNote returns cm of metric height (input 0 for in if needed)
     */
    public static double getImperialHeight(int ft, int in) {
        int totalIn = ftToIn(ft) + in;

        return inToCm(totalIn);
    }

    /**
     *
     * @apiNote returns kg of metric weight
     */
    public static double getImperialWeight(double lbs) {
        return lbs / LBS_TO_KG;
    }

    /**
     *
     * @apiNote returns cm of metric inch
     */
    private static double inToCm(int in) {
        return in * IN_TO_CM;
    }

    /**
     *
     * @apiNote returns in of metric feet
     */
    private static int ftToIn(int ft) {
        return ft * FT_TO_IN;
    }
}
