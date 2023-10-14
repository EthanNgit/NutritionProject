package com.example.nutritionproject.Custom.java.Custom;

import android.util.Log;

import java.time.LocalDateTime;

public class CustomConversionMethods
{
    private static final double LBS_TO_KG = 2.205;
    private static final double CAL_IN_LBS = 3500;
    private static final double CAL_IN_KGS = 7700;
    private static final double IN_TO_CM = 2.54;
    private static final int FT_TO_IN = 12;

    /**
     * @apiNote returns cm of metric height (input 0 for in if needed)
     */
    public static double getImperialHeight(int feet, int inch)
    {
        int totalInches = feetToInch(feet) + inch;

        return inchToCentimeter(totalInches);
    }

    /**
     * @apiNote returns kg of metric weight
     */
    public static double getImperialWeight(double pounds)
    {
        double kilograms = pounds / LBS_TO_KG;

        return kilograms;
    }

    /**
     * @apiNote returns cm of metric inch
     */
    private static double inchToCentimeter(int inch)
    {
        double centimeters = inch * IN_TO_CM;

        return centimeters;
    }

    /**
     * @apiNote returns in of metric feet
     */
    private static int feetToInch(int feet)
    {
        int inches = feet * FT_TO_IN;

        return inches;
    }

    /**
     * @apiNote returns calories that would make n amount of pounds per week
     */
    public static double getAverageCaloriesPerDayFromLbsPerWeek(double pounds)
    {
        double dailyAverageCaloriesInPounds = (pounds * CAL_IN_LBS) / 7;

        return dailyAverageCaloriesInPounds;
    }

    /**
     * @apiNote returns calories that would make n amount of kilograms per week
     */
    public static double getAverageCaloriesPerDayFromKgsPerWeek(double kgs)
    {
        double dailyAverageCaloriesInKilograms = (kgs * CAL_IN_KGS) / 7;

        return dailyAverageCaloriesInKilograms;
    }

    /**
     * @param calories the calories it should calculate percentages from
     * @param split the percent of each macro indexing 0 : protein, 1 : carb, 2 : fat
     */
    public static int[] getMacrosFromSplit(int calories, int[] split)
    {
        double proteinInGrams = ((calories * ((double)split[0] / 100)) / 4);
        double carbInGrams = ((calories * ((double)split[1] / 100)) / 4);
        double fatInGrams = ((calories * ((double)split[2] / 100)) / 9);

        int[] macros = new int[3];

        macros[0] = (int) Math.floor(proteinInGrams);
        macros[1] = (int) Math.floor(carbInGrams);
        macros[2] = (int) Math.floor(fatInGrams);

        return macros;
    }

    /**
     * @return String in form of HH:MM:SS always 2 digit length
     */
    public static String getHourMinuteSecond()
    {
        LocalDateTime now = LocalDateTime.now();
        String hour = String.valueOf(now.getHour());
        String minute = String.valueOf(now.getMinute());
        String second = String.valueOf(now.getSecond());

        hour = hour.length() != 2 ? "0" + hour : hour;
        minute = minute.length() != 2 ? "0" + minute : minute;
        second = second.length() != 2 ? "0" + second : second;

        Log.d("north", hour + "-" + minute + "-" + second);

        return (hour + "-" + minute + "-" + second);
    }


    /**
     * @param militaryTime The time in military format (hh:mm)
     */
    public static String convertMilitaryTimeToStandardTime(String militaryTime)
    {
        if (militaryTime.length() != 5) return null;

        String standardTime = "00:00 am";
        boolean isPm = false;

        int hour = Integer.parseInt(militaryTime.substring(0,2));
        String minute = militaryTime.substring(militaryTime.length() - 2);

        if (hour >= 12)
        {
            isPm = true;

            if (hour != 12)
            {
                hour = hour - 12;
            }
        }

        standardTime = hour + ":" + minute + (isPm ? " pm" : " am");

        return standardTime;
    }
}
