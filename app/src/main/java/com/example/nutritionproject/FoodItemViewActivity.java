package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUtilityMethods;
import com.example.nutritionproject.Custom.java.Enums.FoodTag;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodNutrition;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class FoodItemViewActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener  {

    private CustomDBMethods dbManager = new CustomDBMethods();

    private BottomNavigationView bottomNavView;

    private ImageView backBtn;

    private FoodProfile baseItem;

    private TextView header;

    //region facts
    private LinearLayout servingSizeLyt;
    private TextView servingSizeValue;
    private LinearLayout calorieLyt;
    private TextView calorieValue;
    private LinearLayout fatLyt;
    private TextView totalFatValue;
    private TextView saturatedFatValue;
    private TextView transFatValue;
    private LinearLayout cholesterolLyt;
    private TextView cholesterolValue;
    private LinearLayout sodiumLyt;
    private TextView sodiumValue;
    private LinearLayout potassiumLyt;
    private TextView potassiumValue;
    private LinearLayout carbLyt;
    private TextView totalCarbValue;
    private TextView dietaryFiberValue;
    private TextView totalSugarValue;
    private TextView addedSugarValue;
    private LinearLayout proteinLyt;
    private TextView proteinValue;

    private ImageView subServingBtn;
    private EditText servingSizeField;
    private ImageView addServingBtn;

    private Button addBtn;


    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();

    //endregion

    private double macroRatio = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_view);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);
        CustomUIMethods.hideKeyboard(this);

        viewSetup();

        Intent thisIntent = getIntent();

        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("name") != null) {
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

            baseItem = new FoodProfile(upcId, itemName, tags, dateAdded, common, brand, verified, nutrition);

            //Log.d("North", baseItem.nutrition.nutrients.get(Nutrient.Protein).first.toString());

            setUI();
        } else {
            //if (CustomUtilityMethods.shouldDebug(this.getClass())) Log.d("NORTH_RECYCLERVIEW", "Food item view failed to open");
            finish();
        }
    }

    private void viewSetup() {
        header = findViewById(R.id.header);

        backBtn = findViewById(R.id.backButton);

        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        servingSizeLyt = findViewById(R.id.serving_size_layout);
        servingSizeValue = findViewById(R.id.serving_size_value);
        calorieLyt = findViewById(R.id.calorie_layout);
        calorieValue = findViewById(R.id.calorie_value);
        fatLyt = findViewById(R.id.fat_layout);
        totalFatValue = findViewById(R.id.total_fat_value);
        saturatedFatValue = findViewById(R.id.saturated_fat_value);
        transFatValue = findViewById(R.id.trans_fat_value);
        cholesterolLyt = findViewById(R.id.cholesterol_layout);
        cholesterolValue = findViewById(R.id.cholesterol_value);
        sodiumLyt = findViewById(R.id.sodium_layout);
        sodiumValue = findViewById(R.id.sodium_value);
        potassiumLyt = findViewById(R.id.potassium_layout);
        potassiumValue = findViewById(R.id.potassium_value);
        carbLyt = findViewById(R.id.carbs_layout);
        totalCarbValue = findViewById(R.id.total_carb_value);
        saturatedFatValue = findViewById(R.id.saturated_fat_value);
        dietaryFiberValue = findViewById(R.id.dietary_fiber_value);
        totalSugarValue = findViewById(R.id.total_sugar_value);
        addedSugarValue = findViewById(R.id.added_sugar_value);
        proteinLyt = findViewById(R.id.protein_layout);
        proteinValue = findViewById(R.id.protein_value);

        servingSizeField = findViewById(R.id.servingSizeTextField);
        subServingBtn = findViewById(R.id.subServingBtn);
        addServingBtn = findViewById(R.id.addServingBtn);

        addBtn = findViewById(R.id.addBtn);

        nutrientToTextMap.put(Nutrient.Calorie, calorieValue);
        nutrientToTextMap.put(Nutrient.TotalFat, totalFatValue);
        nutrientToTextMap.put(Nutrient.SaturatedFat, saturatedFatValue);
        nutrientToTextMap.put(Nutrient.TransFat, transFatValue);
        nutrientToTextMap.put(Nutrient.Cholesterol, cholesterolValue);
        nutrientToTextMap.put(Nutrient.Sodium, sodiumValue);
        nutrientToTextMap.put(Nutrient.Potassium, potassiumValue);
        nutrientToTextMap.put(Nutrient.TotalCarb, totalCarbValue);
        nutrientToTextMap.put(Nutrient.DietaryFiber, dietaryFiberValue);
        nutrientToTextMap.put(Nutrient.TotalSugar, totalSugarValue);
        nutrientToTextMap.put(Nutrient.AddedSugar, addedSugarValue);
        nutrientToTextMap.put(Nutrient.Protein, proteinValue);

        potassiumLyt.setVisibility(View.GONE);

        backBtn.setOnClickListener(this);
        subServingBtn.setOnClickListener(this);
        addServingBtn.setOnClickListener(this);

        addBtn.setOnClickListener(this);

        servingSizeField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    macroRatio = Double.valueOf(s.toString());
                    setUI();
                } catch (NumberFormatException e) {
                    // no error
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUI() {
        //if (CustomUtilityMethods.shouldDebug(this.getClass())) Log.d("NORTH_RECYCLERVIEW", "Food item view opened successfully");
        header.setText(baseItem.name);

        servingSizeValue.setText(baseItem.nutrition.servingSize * macroRatio + " " + baseItem.nutrition.servingMeasurement);

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> itemNutrients = baseItem.nutrition.nutrients;
        for (Nutrient nutrient: itemNutrients.keySet()) {

            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("#####.#", otherSymbols);
            nutrientToTextMap.get(nutrient).setText(df.format((itemNutrients.get(nutrient).first) * macroRatio) + ((itemNutrients.get(nutrient).second != NutrientMeasurement.none) ? itemNutrients.get(nutrient).second.name() : ""));
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        }

        if (id == subServingBtn.getId()) {
            if (macroRatio > 1) {
                macroRatio--;
                servingSizeField.setText(String.valueOf(macroRatio));
            }
        } else if (id == addServingBtn.getId()) {
            macroRatio++;
            servingSizeField.setText(String.valueOf(macroRatio));
        }

        if (id == addBtn.getId()) {
            // Add a new meal to user profile ref
            FoodProfile newItem = baseItem;

            HashMap<Nutrient, Pair<Double, NutrientMeasurement>> newNutrientsMap = new HashMap<>();
            HashMap<Nutrient, Pair<Double, NutrientMeasurement>> oldNutrientsMap = newItem.nutrition.nutrients;
            for (Nutrient nutrient : oldNutrientsMap.keySet()) {
                newNutrientsMap.put(nutrient, new Pair<>(oldNutrientsMap.get(nutrient).first * macroRatio, oldNutrientsMap.get(nutrient).second));
            }
            newItem.nutrition.nutrients = newNutrientsMap;

            MealProfile newMeal = new MealProfile(newItem.name, new ArrayList<>(Arrays.asList(newItem)));

            dbManager.updateUserNutritionWithMeal(newMeal);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }
}