package com.example.nutritionproject.Custom.java.Custom;

import android.util.Log;

import java.time.LocalDateTime;

public class CustomConversionMethods {
    private static final double LBS_TO_KG = 2.205;
    private static final int FT_TO_IN = 12;
    private static final double IN_TO_CM = 2.54;
    private static final double CAL_IN_LBS = 3500;
    private static final double CAL_IN_KGS = 7700;

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

    /**
     *
     * @apiNote returns calories that would make n amount of pounds per week
     */
    public static double getAverageCaloriesPerDayFromLbsPerWeek(double lbs) {
        return (lbs * CAL_IN_LBS) / 7;
    }

    /**
     *
     * @apiNote returns calories that would make n amount of kilograms per week
     */
    public static double getAverageCaloriesPerDayFromKgsPerWeek(double kgs) {
        return (kgs * CAL_IN_KGS) / 7;
    }

    public static int[] getMacrosFromSplit(int calories, int[] split) {
        int[] res = new int[3];

        double proteinG = ((calories * ((double)split[0] / 100)) / 4);
        double carbG = ((calories * ((double)split[1] / 100)) / 4);
        double fatG = ((calories * ((double)split[2] / 100)) / 9);

        res[0] = (int) Math.floor(proteinG);
        res[1] = (int) Math.floor(carbG);
        res[2] = (int) Math.floor(fatG);

        Log.d("NORTH", "Current Split " + calories + "cal " + res[0] + "p " + res[1] + "c " + res[2] + "f ");
        return res;
    }

    /**
     *
     * @return String in form of HH:MM:SS always 2 digit length
     */
    public static String getHourMinuteSecond() {
        LocalDateTime now = LocalDateTime.now();
        String hour = String.valueOf(now.getHour());
        String minute = String.valueOf(now.getMinute());
        String second = String.valueOf(now.getSecond());

        hour = hour.length() != 2 ? "0" + hour : hour;
        minute = minute.length() != 2 ? "0" + minute : minute;
        second = second.length() != 2 ? "0" + second : second;

        return (hour + "-" + minute + "-" + second);

    }


    /**
     *
     * @param militaryTime The time in military format (hh:mm)
     */
    public static String convertMilitaryTimeToStandardTime(String militaryTime) {
        if (militaryTime.length() != 5) {
            return null;
        }
        String standardTime = "00:00 am";
        boolean isPm = false;

        int hour = Integer.parseInt(militaryTime.substring(0,2));
        int minute = Integer.parseInt(militaryTime.substring(militaryTime.length() - 2));

        if (hour >= 12) {
            isPm = true;
            if (hour != 12) {
                hour = hour - 12;
            }
        }

        standardTime = hour + ":" + minute + (isPm ? " pm" : " am");

        return standardTime;
    }
}
