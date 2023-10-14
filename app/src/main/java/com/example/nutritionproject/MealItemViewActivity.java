package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.databinding.ActivityDashboardHomeBinding;
import com.example.nutritionproject.databinding.ActivityMealItemViewBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class MealItemViewActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener
{
    private CustomDBMethods dbManager = new CustomDBMethods();
    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();
    private MealProfile meal;
    private ActivityMealItemViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMealItemViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        CustomUIMethods.hideKeyboard(this);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.searchBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        nutrientToTextMap.put(Nutrient.Calorie, binding.calorieValue);
        nutrientToTextMap.put(Nutrient.TotalFat, binding.totalFatValue);
        nutrientToTextMap.put(Nutrient.SaturatedFat, binding.saturatedFatValue);
        nutrientToTextMap.put(Nutrient.TransFat, binding.transFatValue);
        nutrientToTextMap.put(Nutrient.Cholesterol, binding.cholesterolValue);
        nutrientToTextMap.put(Nutrient.Sodium, binding.sodiumValue);
        nutrientToTextMap.put(Nutrient.Potassium, binding.potassiumValue);
        nutrientToTextMap.put(Nutrient.TotalCarb, binding.totalCarbValue);
        nutrientToTextMap.put(Nutrient.DietaryFiber, binding.dietaryFiberValue);
        nutrientToTextMap.put(Nutrient.TotalSugar, binding.totalSugarValue);
        nutrientToTextMap.put(Nutrient.AddedSugar, binding.addedSugarValue);
        nutrientToTextMap.put(Nutrient.Protein, binding.proteinValue);

        binding.potassiumLayout.setVisibility(View.GONE);

        binding.rmvBtn.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);

        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("mealJson") != null)
        {
            Gson gson = new Gson();
            String jsonString = getIntent().getStringExtra("mealJson");

            meal = gson.fromJson(jsonString, MealProfile.class);
        }
        else
        {
            finish();
        }

        updateNutritionFacts();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        return false;
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.rmvBtn.getId())
        {
            removeMeal();
        }
        else if (id == binding.backBtn.getId())
        {
            finish();
        }
    }

    private void removeMeal()
    {
        dbManager.removeMealFromNutrition(meal);

        finish();
    }

    private void updateNutritionFacts()
    {
        binding.header.setText(meal.mealName);

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> totalNutrients = new HashMap<>();

        if (meal.mealComposition.size() >= 1)
        {
            for (FoodProfile ingredient: meal.mealComposition)
            {
                for (Nutrient nutrient: nutrientToTextMap.keySet())
                {
                    Pair<Double, NutrientMeasurement> newPair = new Pair<>(0.0, NutrientMeasurement.g);
                    Pair<Double, NutrientMeasurement> totalValue = totalNutrients.getOrDefault(nutrient, newPair);
                    Pair<Double, NutrientMeasurement> currentValue = ingredient.nutrition.nutrients.getOrDefault(nutrient, newPair);
                    Pair<Double, NutrientMeasurement> newValue = new Pair<>(currentValue.first + totalValue.first, currentValue.second);

                    totalNutrients.put(nutrient, newValue);
                }
            }

            for (Nutrient nutrient: totalNutrients.keySet())
            {
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("#####.#", otherSymbols);
                String calorieValue = df.format(totalNutrients.get(nutrient).first);
                String measurementTag = totalNutrients.get(nutrient).second.name();

                measurementTag = measurementTag != NutrientMeasurement.none.name() ? measurementTag : "";

                nutrientToTextMap.get(nutrient).setText(calorieValue + measurementTag);
            }
        }
        else
        {
            for (Nutrient nutrient: nutrientToTextMap.keySet())
            {
                nutrientToTextMap.get(nutrient).setText("-");
            }
        }
    }

}