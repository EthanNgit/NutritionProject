package com.example.nutritionproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodNutrition;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.UnitCallback;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.Utils;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.Custom.java.Utility.EventContextStrings;
import com.example.nutritionproject.databinding.ActivityAddFoodItemBinding;
import com.example.nutritionproject.databinding.ActivityFoodItemViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.joda.time.LocalDate;

import java.util.HashMap;

public class AddFoodItemActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, NavigationBarView.OnItemSelectedListener
{

    // Extremely bad practice. Only till i figure out how to better send complex data types over intents (Android only allows primitive)
    public static HashMap<Nutrient, Pair<Double, NutrientMeasurement>> receivedMacros;
    private CustomDBMethods dbManager = new CustomDBMethods();
    private boolean isCommon = true;
    private String incUpcid = "";
    private String cameraPermission = Manifest.permission.CAMERA;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->
            {
                if (isGranted)
                {
                    startScanner();
                }
            });

    private ActivityAddFoodItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFoodItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.searchBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.servingSizeTextField.setOnFocusChangeListener(this);
        binding.servingAmtTextField.setOnFocusChangeListener(this);
        binding.itemNameTextField.setOnFocusChangeListener(this);
        binding.brandTextField.setOnFocusChangeListener(this);
        binding.upcTextField.setOnFocusChangeListener(this);

        binding.carouselForwardSecondBtn.setOnClickListener(this);
        binding.carouselForwardFirstBtn.setOnClickListener(this);
        binding.carouselBackSecondBtn.setOnClickListener(this);
        binding.carouselBackThirdBtn.setOnClickListener(this);
        binding.labelScanBtn.setOnClickListener(this);
        binding.brandedBtn.setOnClickListener(this);
        binding.commonBtn.setOnClickListener(this);
        binding.backBtn.setOnClickListener(this);
        binding.addBtn.setOnClickListener(this);

        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("upcid") != null)
        {
            incUpcid = getIntent().getStringExtra("upcid");
            binding.upcTextField.setText(incUpcid);
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
        else if (id == binding.addBtn.getId())
        {
            addItem(new EventCallback()
            {
                @Override
                public void onSuccess(@Nullable EventContext context)
                {
                    finish();
                }

                @Override
                public void onFailure(@Nullable EventContext context)
                {
                    //Error
                }
            });
        }
        else if (id == binding.labelScanBtn.getId())
        {
            requestCameraAndStartScanner();
        }

        if (id == binding.commonBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.commonBtn, binding.brandedBtn, binding.itemTypeTwoWayVisualOne,
                    binding.itemTypeTwoWayVisualTwo, true,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(true);
        }
        else if (id == binding.brandedBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.commonBtn, binding.brandedBtn, binding.itemTypeTwoWayVisualOne,
                    binding.itemTypeTwoWayVisualTwo, false,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(false);
        }

        if (id == binding.carouselForwardFirstBtn.getId())
        {
            setActiveCard(2);
        }
        else if (id == binding.carouselBackSecondBtn.getId())
        {
            setActiveCard(1);
        }
        else if (id == binding.carouselForwardSecondBtn.getId())
        {
            setActiveCard(3);
        }
        else if (id ==  binding.carouselBackThirdBtn.getId())
        {
            setActiveCard(2);
        }
    }

    private void setBrandField(boolean isCommonP)
    {
        int visibilityOption = isCommonP ? View.GONE : View.VISIBLE;

        binding.brandNameLayout.setVisibility(visibilityOption);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus)
    {
        int id = view.getId();

        CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{binding.itemNameTextField, binding.upcTextField,
                binding.brandTextField, binding.servingAmtTextField, binding.servingSizeTextField}, R.drawable.bg_black_2dp_stroke_gray);
        CustomUIMethods.setPopupMessage(this, binding.addItemErrorText, R.color.darkTheme_Error, "");

        if (id == binding.itemNameTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.itemNameTextField}, R.drawable.bg_black_2dp_stroke_white);
        }
        else if (id == binding.upcTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.upcTextField}, R.drawable.bg_black_2dp_stroke_white);
        }
        else if (id == binding.brandTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.brandTextField}, R.drawable.bg_black_2dp_stroke_white);
        }
        else if (id == binding.servingAmtTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.servingAmtTextField}, R.drawable.bg_black_2dp_stroke_white);
        }
        else if (id == binding.servingSizeTextField.getId())
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.servingSizeTextField}, R.drawable.bg_black_2dp_stroke_white);
        }

        if (binding.itemNameTextField.getText().length() != 0 && binding.itemNameTextField.getText().length() <= 1)
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.itemNameTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.nameErrorLabelText, R.color.darkTheme_Error, "Invalid name");
        }
        if (binding.upcTextField.getText().length() != 0 && binding.upcTextField.getText().length() != 12)
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.upcTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.upcErrorLabelText, R.color.darkTheme_Error, "Invalid UPC-A");
        }
        if (binding.brandTextField.getText().length() != 0 && !isCommon && binding.brandTextField.getText().length() <= 1)
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.brandTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.brandErrorLabelText, R.color.darkTheme_Error, "Invalid brand name");
        }
        if (binding.servingAmtTextField.getText().length() != 0 && binding.servingAmtTextField.getText().length() < 1)
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.servingAmtTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.servingErrorLabelText, R.color.darkTheme_Error, "Invalid serving size");
        }
        if (binding.servingSizeTextField.getText().length() != 0 && binding.servingSizeTextField.getText().length() <= 1)
        {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {binding.servingSizeTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, binding.servingErrorLabelText, R.color.darkTheme_Error, "Invalid serving amount");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    private void setActiveCard(int card)
    {
        binding.addCard1.setVisibility(View.GONE);
        binding.addCard2.setVisibility(View.GONE);
        binding.addCard3.setVisibility(View.GONE);

        switch (card)
        {
            case 1:
                binding.addCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.addCard2.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.addCard3.setVisibility(View.VISIBLE);
                if (receivedMacros != null) autoFillMacros();
        }
    }

    private void autoFillMacros()
    {
        if (receivedMacros.get(Nutrient.Calorie) != null)
        {
           binding.calorieTextField.setText(receivedMacros.get(Nutrient.Calorie).first.toString());
        }
        if (receivedMacros.get(Nutrient.Protein) != null)
        {
            binding.proteinTextField.setText(receivedMacros.get(Nutrient.Protein).first.toString());
        }
        if (receivedMacros.get(Nutrient.TotalCarb) != null)
        {
            binding.carbsTextField.setText(receivedMacros.get(Nutrient.TotalCarb).first.toString());
        }
        if (receivedMacros.get(Nutrient.TotalFat) != null)
        {
            binding.fatTextField.setText(receivedMacros.get(Nutrient.TotalFat).first.toString());
        }
    }

    private void addItem(@Nullable EventCallback callback)
    {
        String itemUpc = binding.upcTextField.getText().toString();
        String itemName = binding.itemNameTextField.getText().toString();
        String brandName = binding.brandTextField.getText().toString();
        String servingSize = binding.servingSizeTextField.getText().toString();
        String servingAmt = binding.servingAmtTextField.getText().toString();
        String calories = binding.calorieTextField.getText().toString();
        String protein = binding.proteinTextField.getText().toString();
        String carbs = binding.carbsTextField.getText().toString();
        String fat = binding.fatTextField.getText().toString();

        if (itemName.isEmpty() || servingAmt.isEmpty() || servingSize.isEmpty())
        {
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());

            return;
        }
        if (!isCommon && brandName.isEmpty())
        {
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());

            return;
        }

        itemUpc = !itemUpc.isEmpty() ? itemUpc : "";
        calories = !calories.isEmpty() ? calories : "0";
        protein = !protein.isEmpty() ? protein : "0";
        carbs = !carbs.isEmpty() ? carbs : "0";
        fat = !fat.isEmpty() ? fat : "0";

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> newItemNutrients = new HashMap<>();

        if (receivedMacros == null)
        {
            newItemNutrients.put(Nutrient.Calorie, new Pair<>(Double.valueOf(calories), NutrientMeasurement.none));
            newItemNutrients.put(Nutrient.Protein, new Pair<>(Double.valueOf(protein), NutrientMeasurement.g));
            newItemNutrients.put(Nutrient.TotalCarb, new Pair<>(Double.valueOf(carbs), NutrientMeasurement.g));
            newItemNutrients.put(Nutrient.TotalFat, new Pair<>(Double.valueOf(fat), NutrientMeasurement.g));
        }
        else
        {
            newItemNutrients = receivedMacros;

            if (newItemNutrients.get(Nutrient.Calorie) == null)
            {
                newItemNutrients.put(Nutrient.Calorie, new Pair<>(Double.valueOf(calories), NutrientMeasurement.none));
            }
            else if (newItemNutrients.get(Nutrient.Protein) == null)
            {
                newItemNutrients.put(Nutrient.Protein, new Pair<>(Double.valueOf(protein), NutrientMeasurement.g));
            }
            else if (newItemNutrients.get(Nutrient.TotalCarb) == null)
            {
                newItemNutrients.put(Nutrient.TotalCarb, new Pair<>(Double.valueOf(carbs), NutrientMeasurement.g));
            }
            else if (newItemNutrients.get(Nutrient.TotalFat) == null)
            {
                newItemNutrients.put(Nutrient.TotalFat, new Pair<>(Double.valueOf(fat), NutrientMeasurement.g));
            }
        }

        FoodNutrition newItemNutrition = new FoodNutrition(newItemNutrients.get(Nutrient.Calorie).first, Double.valueOf(servingAmt), servingSize, newItemNutrients);
        FoodProfile newItem = new FoodProfile(itemUpc, itemName, null, String.valueOf(new LocalDate()), isCommon, brandName, false, newItemNutrition);

        dbManager.addFoodItem(newItem, new EventCallback()
        {
            @Override
            public void onSuccess(@Nullable EventContext context)
            {
                callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.success).build());
            }

            @Override
            public void onFailure(@Nullable EventContext context)
            {
                callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
            }
        });
    }

    private void requestCameraAndStartScanner()
    {
        int currentPermission = ContextCompat.checkSelfPermission(this, cameraPermission);

        if (currentPermission == PackageManager.PERMISSION_GRANTED)
        {
            startScanner();
        }
        else
        {
            requestCameraPermission();
        }
    }

    private void startScanner()
    {
        NutritionScanActivity.startScanner(this, new UnitCallback()
        {
            @Override
            public void onSuccess()
            {

            }
        });
    }

    private void requestCameraPermission()
    {
        if (shouldShowRequestPermissionRationale(cameraPermission))
        {
            Context parentContext = this;
            Utils.cameraPermissionRequest(this, new UnitCallback()
            {
                @Override
                public void onSuccess()
                {
                    Utils.openPermissionSettings(parentContext);
                }
            });
        }
        else
        {
            requestPermissionLauncher.launch(cameraPermission);
        }
    }
}