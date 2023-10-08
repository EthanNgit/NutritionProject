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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.joda.time.LocalDate;

import java.util.HashMap;

public class AddFoodItemActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, NavigationBarView.OnItemSelectedListener {

    // Extremely bad practice. Only till i figure out how to better send complex data types over intents (Android only allows primitive)
    public static HashMap<Nutrient, Pair<Double, NutrientMeasurement>> receivedMacros;

    private CustomDBMethods dbManager = new CustomDBMethods();

    private ImageView backBtn;

    private BottomNavigationView bottomNavView;

    //region Card 1
    private CardView addCard1;
    private TextView addItemErrorText;

    private EditText itemNameTextField;
    private TextView itemNameErrorText;

    private EditText itemUPCTextField;
    private TextView itemUPCErrorText;

    private TextView itemTypeCommonTwoWayButton;
    private TextView itemTypeBrandedTwoWayButton;
    private ImageView itemTypeWayVisualOne;
    private ImageView itemTypeWayVisualTwo;

    private LinearLayout itemBrandLayout;
    private EditText itemBrandTextField;
    private TextView itemBrandErrorText;

    private LinearLayout firstCarouselNextButton;
    //endregion
    //region Card 2
    private CardView addCard2;

    private EditText servingAmtTextField;
    private EditText servingSizeTextField;
    private TextView servingErrorTextField;

    private CardView scanLabelButton;

    private LinearLayout secondCarouselBackButton;
    private LinearLayout secondCarouselNextButton;

    //endregion
    //region Card3
    private CardView addCard3;

    private EditText itemCalorieTextField;
    private TextView itemCalorieErrorText;

    private EditText itemProteinTextField;
    private TextView itemProteinErrorText;

    private EditText itemCarbTextField;
    private TextView itemCarbErrorText;

    private EditText itemFatTextField;
    private TextView itemFatErrorText;

    private LinearLayout thirdCarouselBackButton;
    //endregion

    private boolean isCommon = true;

    private Button addButton;

    private String incUpcid = "";

    private String cameraPermission = Manifest.permission.CAMERA;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startScanner();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);


        //region Searching and setting
        //region Frame items
        backBtn = findViewById(R.id.backButton);
        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);
        //endregion
        //region Finding card1
        addCard1 = findViewById(R.id.addItemCard1);

        itemNameTextField = findViewById(R.id.itemNameTextField);
        itemNameErrorText = findViewById(R.id.nameErrorLabelText);
        itemUPCTextField = findViewById(R.id.upcTextField);
        itemUPCErrorText = findViewById(R.id.upcErrorLabelText);

        itemTypeCommonTwoWayButton = findViewById(R.id.commonButton);
        itemTypeBrandedTwoWayButton = findViewById(R.id.brandedButton);
        itemTypeWayVisualOne = findViewById(R.id.itemTypeTwoWayVisualOne);
        itemTypeWayVisualTwo = findViewById(R.id.itemTypeTwoWayVisualTwo);

        itemBrandLayout = findViewById(R.id.brandNameLayout);
        itemBrandTextField = findViewById(R.id.brandTextField);
        itemBrandErrorText = findViewById(R.id.brandErrorLabelText);
        //endregion
        //region Finding card2
        addCard2 = findViewById(R.id.addCard2);
        servingAmtTextField = findViewById(R.id.servingAmtTextField);
        servingSizeTextField = findViewById(R.id.servingSizeTextField);
        servingErrorTextField = findViewById(R.id.servingErrorLabelText);
        scanLabelButton = findViewById(R.id.labelScanButton);
        //endregion

        //region Finding card3
        addCard3 = findViewById(R.id.addCard3);
        itemCalorieTextField = findViewById(R.id.calorieTextField);
        itemCalorieErrorText = findViewById(R.id.caloriesErrorLabelText);

        itemProteinTextField = findViewById(R.id.proteinTextField);
        itemProteinErrorText = findViewById(R.id.proteinErrorLabelText);

        itemCarbTextField = findViewById(R.id.carbsTextField);
        itemCarbErrorText = findViewById(R.id.carbsErrorLabelText);

        itemFatTextField = findViewById(R.id.fatTextField);
        itemFatErrorText = findViewById(R.id.fatErrorLabelText);
        //endregion
        //region Finding rest
        addButton = findViewById(R.id.addBtn);
        addItemErrorText = findViewById(R.id.addItemErrorText);

        firstCarouselNextButton = findViewById(R.id.carouselForwardFirstButton);
        secondCarouselBackButton = findViewById(R.id.carouselBackSecondButton);
        secondCarouselNextButton = findViewById(R.id.carouselForwardSecondButton);
        thirdCarouselBackButton = findViewById(R.id.carouselBackThirdButton);

        //endregion
        //region Setting listeners
        itemNameTextField.setOnFocusChangeListener(this);
        itemUPCTextField.setOnFocusChangeListener(this);
        itemBrandTextField.setOnFocusChangeListener(this);

        servingAmtTextField.setOnFocusChangeListener(this);
        servingSizeTextField.setOnFocusChangeListener(this);

        addButton.setOnClickListener(this);
        scanLabelButton.setOnClickListener(this);

        itemTypeBrandedTwoWayButton.setOnClickListener(this);
        itemTypeCommonTwoWayButton.setOnClickListener(this);

        firstCarouselNextButton.setOnClickListener(this);
        secondCarouselBackButton.setOnClickListener(this);
        secondCarouselNextButton.setOnClickListener(this);
        thirdCarouselBackButton.setOnClickListener(this);
        //endregion
        //endregion

        Intent intent = this.getIntent();
        if (intent != null && intent.getStringExtra("upcid") != null) {
            incUpcid = getIntent().getStringExtra("upcid");
            itemUPCTextField.setText(incUpcid);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            //Give Warning about save if the fields changed
            finish();
        } else if (id == addButton.getId()) {
            addItem(new EventCallback() {
                @Override
                public void onSuccess(@Nullable EventContext context) {
                    finish();
                }

                @Override
                public void onFailure(@Nullable EventContext context) {
                    //Error
                }
            });
        } else if (id == scanLabelButton.getId()) {
            requestCameraAndStartScanner();
        }

        if (id == itemTypeCommonTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, itemTypeCommonTwoWayButton, itemTypeBrandedTwoWayButton, itemTypeWayVisualOne, itemTypeWayVisualTwo, true,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(true);
        } else if (id == itemTypeBrandedTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, itemTypeCommonTwoWayButton, itemTypeBrandedTwoWayButton, itemTypeWayVisualOne, itemTypeWayVisualTwo, false,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(false);
        }

        if (id == firstCarouselNextButton.getId()) {
            setActiveCard(2);
        } else if (id == secondCarouselBackButton.getId()) {
            setActiveCard(1);
        } else if (id == secondCarouselNextButton.getId()) {
            setActiveCard(3);
        } else if (id == thirdCarouselBackButton.getId()) {
            setActiveCard(2);
        }
    }

    private void setBrandField(boolean isCommonP) {
        isCommon = isCommonP;

        if (isCommon) {
            itemBrandLayout.setVisibility(View.GONE);
        } else {
            itemBrandLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();

        CustomUIMethods.setTextFieldBackgrounds(this, new EditText[]{itemNameTextField, itemUPCTextField, itemBrandTextField, servingAmtTextField, servingSizeTextField}, R.drawable.bg_black_2dp_stroke_gray);
        CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "");

        if (id == itemNameTextField.getId()) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemNameTextField}, R.drawable.bg_black_2dp_stroke_white);
        } else if (id == itemUPCTextField.getId()) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemUPCTextField}, R.drawable.bg_black_2dp_stroke_white);
        } else if (id == itemBrandTextField.getId()) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemBrandTextField}, R.drawable.bg_black_2dp_stroke_white);
        } else if (id == servingSizeTextField.getId()) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {servingSizeTextField}, R.drawable.bg_black_2dp_stroke_white);
        } else if (id == servingAmtTextField.getId()) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {servingAmtTextField}, R.drawable.bg_black_2dp_stroke_white);
        }

        if (itemNameTextField.getText().length() != 0 && itemNameTextField.getText().length() <= 1) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemNameTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "Invalid name");
        }
        if (itemUPCTextField.getText().length() != 0 && itemUPCTextField.getText().length() != 12) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemUPCTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "Invalid UPC-A");
        }
        if (itemBrandTextField.getText().length() != 0 && !isCommon && itemBrandTextField.getText().length() <= 1) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {itemBrandTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "Invalid brand name");
        }
        if (servingSizeTextField.getText().length() != 0 && itemNameTextField.getText().length() < 1) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {servingSizeTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "Invalid serving size");
        }
        if (servingAmtTextField.getText().length() != 0 && itemNameTextField.getText().length() <= 1) {
            CustomUIMethods.setTextFieldBackgrounds(this, new EditText[] {servingAmtTextField}, R.drawable.bg_black_2dp_stroke_red);
            CustomUIMethods.setPopupMessage(this, addItemErrorText, R.color.darkTheme_Error, "Invalid serving amount");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);
        return false;
    }

    private void setActiveCard(int card) {
        addCard1.setVisibility(View.GONE);
        addCard2.setVisibility(View.GONE);
        addCard3.setVisibility(View.GONE);

        switch (card) {
            case 1:
                addCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                addCard2.setVisibility(View.VISIBLE);
                break;
            case 3:
                addCard3.setVisibility(View.VISIBLE);
                if (receivedMacros != null) autoFillMacros();

        }
    }

    private void autoFillMacros() {
        if (receivedMacros.get(Nutrient.Calorie) != null) {
            itemCalorieTextField.setText(receivedMacros.get(Nutrient.Calorie).first.toString());
            Log.d("NORTH", "CAL SET");
        }
        if (receivedMacros.get(Nutrient.Protein) != null) {
            itemProteinTextField.setText(receivedMacros.get(Nutrient.Protein).first.toString());
            Log.d("NORTH", "PROTEin SET");
        }
        if (receivedMacros.get(Nutrient.TotalCarb) != null) {
            itemCarbTextField.setText(receivedMacros.get(Nutrient.TotalCarb).first.toString());
            Log.d("NORTH", "CARB SET");
        }
        if (receivedMacros.get(Nutrient.TotalFat) != null) {
            itemFatTextField.setText(receivedMacros.get(Nutrient.TotalFat).first.toString());
            Log.d("NORTH", "FAT SET");
        }
    }

    private void addItem(@Nullable EventCallback callback) {
        //TODO: make form more advanced for adding vitamins and types of, perhaps by scanning nutrition label
        String itemName = itemNameTextField.getText().toString();
        String itemUpc = itemUPCTextField.getText().toString();
        //Tags
        String brandName = itemBrandTextField.getText().toString();
        String servingAmt = servingAmtTextField.getText().toString();
        String servingSize = servingSizeTextField.getText().toString();
        String calories = itemCalorieTextField.getText().toString();
        String protein = itemProteinTextField.getText().toString();
        String carbs = itemCarbTextField.getText().toString();
        String fat = itemFatTextField.getText().toString();

        if (itemName.isEmpty() || servingAmt.isEmpty() || servingSize.isEmpty()) {
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());
            return;
        }
        if (!isCommon && brandName.isEmpty()){
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());
            return;
        }

        itemUpc = !itemUpc.isEmpty() ? itemUpc : "";
        calories = !calories.isEmpty() ? calories : "0";
        protein = !protein.isEmpty() ? protein : "0";
        carbs = !carbs.isEmpty() ? carbs : "0";
        fat = !fat.isEmpty() ? fat : "0";

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> newItemNutrients = new HashMap<>();
        if (receivedMacros == null) {
            //TODO: check duplicate to see if it already exists in advanced setting
            newItemNutrients.put(Nutrient.Calorie, new Pair<>(Double.valueOf(calories), NutrientMeasurement.none));
            newItemNutrients.put(Nutrient.Protein, new Pair<>(Double.valueOf(protein), NutrientMeasurement.g));
            newItemNutrients.put(Nutrient.TotalCarb, new Pair<>(Double.valueOf(carbs), NutrientMeasurement.g));
            newItemNutrients.put(Nutrient.TotalFat, new Pair<>(Double.valueOf(fat), NutrientMeasurement.g));

        } else {
            newItemNutrients = receivedMacros;

            if (newItemNutrients.get(Nutrient.Calorie) == null) {
                newItemNutrients.put(Nutrient.Calorie, new Pair<>(Double.valueOf(calories), NutrientMeasurement.none));
            } else if (newItemNutrients.get(Nutrient.Protein) == null) {
                newItemNutrients.put(Nutrient.Protein, new Pair<>(Double.valueOf(protein), NutrientMeasurement.g));
            } else if (newItemNutrients.get(Nutrient.TotalCarb) == null) {
                newItemNutrients.put(Nutrient.TotalCarb, new Pair<>(Double.valueOf(carbs), NutrientMeasurement.g));
            } else if (newItemNutrients.get(Nutrient.TotalFat) == null) {
                newItemNutrients.put(Nutrient.TotalFat, new Pair<>(Double.valueOf(fat), NutrientMeasurement.g));
            }
        }

        FoodNutrition newItemNutrition = new FoodNutrition(Double.valueOf(calories), Double.valueOf(servingAmt), servingSize, newItemNutrients);
        FoodProfile newItem = new FoodProfile(itemUpc, itemName, null, String.valueOf(new LocalDate()), isCommon, brandName, false, newItemNutrition);


        dbManager.addFoodItem(newItem, new EventCallback() {
            @Override
            public void onSuccess(@Nullable EventContext context) {
                callback.onSuccess(new EventContext.Builder().withMessage(EventContextStrings.success).build());
            }

            @Override
            public void onFailure(@Nullable EventContext context) {
                callback.onFailure(new EventContext.Builder().withError(EventContextStrings.responseError).build());
            }
        });


    }

    private void requestCameraAndStartScanner() {
        if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            startScanner();
        } else {
            requestCameraPermission();
        }
    }

    private void startScanner() {
        NutritionScanActivity.startScanner(this, new UnitCallback() {
            @Override
            public void onSuccess() {

            }
        });
    }

    private void requestCameraPermission() {
        if (shouldShowRequestPermissionRationale(cameraPermission)) {
            Context parentContext = this;
            Utils.cameraPermissionRequest(this, new UnitCallback() {
                @Override
                public void onSuccess() {
                    Utils.openPermissionSettings(parentContext);
                }
            });

        } else {
            requestPermissionLauncher.launch(cameraPermission);
        }
    }

}