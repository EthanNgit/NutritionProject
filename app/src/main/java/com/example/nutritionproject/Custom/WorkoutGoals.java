package com.example.nutritionproject.Custom;

public enum WorkoutGoals {
    HeavyCut(-1000),
    LightCut(-500),
    Maintain(0),
    LightBulk(250),
    HeavyBulk(500);

    private final int difference;

    private WorkoutGoals(int difference) {
        this.difference = difference;
    }

    public double getDifference() {
        return difference;
    }
}
