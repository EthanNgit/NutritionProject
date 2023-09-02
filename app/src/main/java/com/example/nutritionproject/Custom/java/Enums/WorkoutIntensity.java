package com.example.nutritionproject.Custom.java.Enums;

/**
 * @apiNote Each level of activity multiplies the bmr by a value to get a representation of their burned non bmr calories
 */
public enum WorkoutIntensity {
    Sedentary(1.2),
    Light(1.38),
    Moderate(1.55),
    Heavy(1.72),
    Athlete(1.9);

    private final double multiplier;

    private WorkoutIntensity(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

}