package com.example.nutritionproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.FoodListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.IngredientListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.WidgetObject;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.databinding.ActivityAddMealItemBinding;
import com.example.nutritionproject.Custom.java.Enums.ActivityResultCodes;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
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

public class AddMealItemActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{
    private final static int RECENT_MEAL_HISTORY_MAX_SIZE = 5;
    private CustomDBMethods dbManager = new CustomDBMethods();
    private MealProfile thisMeal;
    private ArrayList<FoodProfile> ingredients = new ArrayList<>();
    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();

    private ActivityAddMealItemBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMealItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.addBtn.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);
        binding.dropdownBtn.setOnClickListener(this);
        binding.ingredientsSearchBtn.setOnClickListener(this);

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

        Context parentContext = this;
        binding.itemNameTextField.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                int id = view.getId();

                if (id == binding.itemNameTextField.getId())
                {
                    CustomUIMethods.setTextFieldBackgrounds(parentContext, new EditText[] {binding.itemNameTextField}, R.drawable.bg_black_2dp_stroke_white);
                }
            }
        });
    }

    ActivityResultLauncher<Intent> launchActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == ActivityResultCodes.SearchView.getCode())
                {
                    Intent data = result.getData();
                    Gson gson = new Gson();

                    String ingredientJson = data.getStringExtra("ingredient");
                    FoodProfile ingredient = gson.fromJson(ingredientJson, FoodProfile.class);
                    Log.d("NORTH", ingredientJson);

                    ingredients.add(ingredient);
                    updateIngredientRecyclerView();
                    updateNutritionFacts();
                }
            });

    private void searchIngredient()
    {
        Intent intent = new Intent(this, SearchActivity.class);

        intent.putExtra("searchForIngredient", true);
        launchActivityResultLauncher.launch(intent);
    }

    private void updateIngredientRecyclerView()
    {
        if (ingredients.size() > 0)
        {
            boolean isGreaterThanOne = ingredients.size() > 1;
            String ingredientTag = isGreaterThanOne ? " ingredients" : " ingredient";
            String ingredientAmount = ingredients.size() + ingredientTag;
            RecyclerViewInterface recyclerViewInterface = this;
            IngredientListAdapter adapter = new IngredientListAdapter(this, ingredients, recyclerViewInterface);

            binding.mealIngredientsRecyclerView.setAdapter(adapter);
            binding.mealIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.ingredientsHeaderTextLabel.setText(ingredientAmount);
            binding.mealIngredientsRecyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            String ingredientAmount = "No ingredients added";

            binding.ingredientsHeaderTextLabel.setText(ingredientAmount);
            binding.mealIngredientsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void updateNutritionFacts()
    {
        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> totalNutrients = new HashMap<>();

        if (ingredients.size() >= 1)
        {
            for (FoodProfile ingredient: ingredients)
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

    private void addMeal()
    {
        String mealName = binding.itemNameTextField.getText().toString();

        if (mealName.length() > 1 && ingredients.size() >= 1)
        {
            //TODO: Add to a personal database slice of their recipes.
            thisMeal = new MealProfile(mealName, ingredients);

            dbManager.updateUserNutritionWithMeal(thisMeal);

            addMealToPreferences();

            finish();
        }
    }

    private void addMealToPreferences()
    {
        Gson gson = new Gson();

        SharedPreferences preferences = getSharedPreferences("meals", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String jsonString = preferences.getString("recent_meals", null);
        Type listType = new TypeToken<ArrayList<MealProfile>>() {}.getType();

        ArrayList<MealProfile> recentMeals = new ArrayList<>();

        if (jsonString != null)
        {
            recentMeals = gson.fromJson(jsonString, listType);
            recentMeals.add(thisMeal);

            while (recentMeals.size() > RECENT_MEAL_HISTORY_MAX_SIZE)
            {
                recentMeals.remove(0);
            }
        }
        else
        {
            recentMeals.add(thisMeal);
        }

        editor.putString("recent_meals", gson.toJson(recentMeals));
        editor.apply();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.addBtn.getId()) {
            addMeal();
        }
        else if (id == binding.backBtn.getId())
        {
            finish();
        }
        else if (id == binding.dropdownBtn.getId())
        {
            CustomUIMethods.toggleDropdown(this, binding.dropdownIcon, binding.mealIngredientNutritionFacts);
        }
        else if (id == binding.ingredientsSearchBtn.getId())
        {
            searchIngredient();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onItemClick(int clickId, int position)
    {
        ingredients.remove(ingredients.get(position));

        updateIngredientRecyclerView();
        updateNutritionFacts();
    }
}