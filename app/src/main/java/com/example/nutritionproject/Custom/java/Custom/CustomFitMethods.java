package com.example.nutritionproject.Custom.java.Custom;


import com.example.nutritionproject.Custom.java.Enums.WorkoutIntensity;

public class CustomFitMethods
{

    /**
     * @param weight user weight (kg)
     * @param height user height (cm)
     * @param age user age
     * @param male user is male?
     */
    public double getBMR(double weight, double height, int age, boolean male)
    {
        double bmrValue = ((10 * weight) + (6.25 * height) - (5 * age) + (male ? 5 : -161));

        return bmrValue;
    }

    /**
     * @param weight user weight (kg)
     * @param height user height (cm)
     * @param age user age
     * @param male user is male?
     * @param intensity level of activity user does
     * @param goal amount user wants to gain or lose (in calories)
     */
    public double getTDEE(double weight, double height, int age, boolean male, WorkoutIntensity intensity, int goal)
    {
        double tdeeValue = (getBMR(weight, height, age, male) * intensity.getMultiplier()) + goal;

        return tdeeValue;
    }
}
