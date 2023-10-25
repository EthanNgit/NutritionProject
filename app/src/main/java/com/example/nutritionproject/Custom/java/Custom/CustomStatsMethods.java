package com.example.nutritionproject.Custom.java.Custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.example.nutritionproject.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import kotlin.Triple;

public class CustomStatsMethods
{
    public static boolean fillPieChart(Context context, PieChart chart, String[] labels, int[] values, int[] colors)
    {
        if (labels.length == values.length && values.length == colors.length && labels.length >= 0)
        {
            chart.clearChart();

            ArrayList<Triple<String, Integer, Integer>> pieSliceData = new ArrayList<>();

            for (int i = 0; i < labels.length; i++)
            {
                Triple<String, Integer, Integer> currentTriple;

                String currentLabel = labels[i];
                int currentValue = values[i];
                int currentColor = context.getColor(colors[i]);

                currentTriple = new Triple<>(currentLabel, currentValue, currentColor);
                pieSliceData.add(currentTriple);
            }

            for (Triple<String, Integer, Integer> sliceData: pieSliceData)
            {
                String sliceLabel = sliceData.component1();
                Integer sliceValue = sliceData.component2();
                Integer sliceColor = sliceData.component3();

                PieModel sliceModel = new PieModel(sliceLabel, sliceValue, sliceColor);
                chart.addPieSlice(sliceModel);
            }
            chart.startAnimation();

            return true;
        }

        return false;
    }

    public static boolean clearPieChart(Context context, PieChart chart, int background)
    {
        chart.clearChart();
        chart.addPieSlice(new PieModel("", 1, ContextCompat.getColor(context, background)));

        return true;
    }
}
