package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.ActivityResultCodes;
import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodNutrition;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.databinding.ActivityFoodItemViewBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class FoodItemViewActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener
{
    private CustomDBMethods dbManager = new CustomDBMethods();
    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();
    private FoodProfile baseItem;
    private double macroRatio = 1;
    private boolean isSearchingIngredient = false;
    private ActivityFoodItemViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodItemViewBinding.inflate(getLayoutInflater());
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

        binding.addBtn.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);
        binding.subServingBtn.setOnClickListener(this);
        binding.addServingBtn.setOnClickListener(this);

        binding.servingSizeTextField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                try
                {
                    macroRatio = Double.valueOf(s.toString());
                    setNutritionFacts();
                }
                catch (NumberFormatException e)
                {
                    // no error
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        Intent thisIntent = getIntent();
        Intent intent = this.getIntent();

        if (intent != null && intent.getStringExtra("name") != null)
        {
            Gson gson = new Gson();
            Type setType = new TypeToken<Set<FoodTag>>(){}.getType();

            String itemName = intent.getStringExtra("name");
            String upcId = intent.getStringExtra("upcid");
            String dateAdded = intent.getStringExtra("dateadded");
            Set<FoodTag> tags = gson.fromJson(intent.getStringExtra("tags"), setType);
            boolean common = intent.getBooleanExtra("common", false);
            String brand = intent.getStringExtra("brand");
            boolean verified = intent.getBooleanExtra("verified", false);
            FoodNutrition nutrition = gson.fromJson(intent.getStringExtra("nutrition"), FoodNutrition.class);
            isSearchingIngredient = intent.getBooleanExtra("isIngredient", false);

            baseItem = new FoodProfile(upcId, itemName, tags, dateAdded, common, brand, verified, nutrition);

            binding.header.setText(baseItem.name);

            setNutritionFacts();

            if (isSearchingIngredient)
            {
                binding.addBtn.setText(getString(R.string.add_meal));
            }
        }
        else
        {
            finish();
        }
    }

    private void setNutritionFacts()
    {
        binding.servingSizeValue.setText(baseItem.nutrition.servingSize * macroRatio + " " + baseItem.nutrition.servingMeasurement);

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> itemNutrients = baseItem.nutrition.nutrients;
        for (Nutrient nutrient: itemNutrients.keySet())
        {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("#####.#", otherSymbols);
            nutrientToTextMap.get(nutrient).setText(df.format((itemNutrients.get(nutrient).first) * macroRatio) +
                    ((itemNutrients.get(nutrient).second != NutrientMeasurement.none) ? itemNutrients.get(nutrient).second.name() : ""));
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
        }

        if (id == binding.subServingBtn.getId())
        {
            if (macroRatio > 1)
            {
                macroRatio--;
                binding.servingSizeTextField.setText(String.valueOf(macroRatio));
            }
        }
        else if (id == binding.addServingBtn.getId())
        {
            macroRatio++;
            binding.servingSizeTextField.setText(String.valueOf(macroRatio));
        }

        if (id == binding.addBtn.getId())
        {
            FoodProfile newItem = baseItem;

            HashMap<Nutrient, Pair<Double, NutrientMeasurement>> newNutrientsMap = new HashMap<>();
            HashMap<Nutrient, Pair<Double, NutrientMeasurement>> oldNutrientsMap = newItem.nutrition.nutrients;

            for (Nutrient nutrient : oldNutrientsMap.keySet())
            {
                newNutrientsMap.put(nutrient, new Pair<>(oldNutrientsMap.get(nutrient).first * macroRatio, oldNutrientsMap.get(nutrient).second));
            }

            newItem.nutrition.nutrients = newNutrientsMap;

            if (isSearchingIngredient) {
                Gson gson = new Gson();
                Intent intent = new Intent();
                intent.putExtra("ingredient", gson.toJson(newItem));
                setResult(ActivityResultCodes.FoodItemView.getCode(), intent);
            }
            else
            {
                MealProfile newMeal = new MealProfile(newItem.name, new ArrayList<>(Arrays.asList(newItem)));
                dbManager.addMealToNutrition(newMeal);
            }

            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this,null, binding.bottomNavigationView, item);

        return false;
    }
}