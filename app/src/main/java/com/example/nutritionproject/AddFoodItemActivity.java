package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.Custom.java.Utility.EventContextStrings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.checkerframework.checker.i18nformatter.qual.I18nMakeFormat;
import org.joda.time.LocalDate;

import java.util.HashMap;

public class AddFoodItemActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, NavigationBarView.OnItemSelectedListener {

    private CustomDBMethods dbManager = new CustomDBMethods();

    private ImageView backBtn;

    private BottomNavigationView bottomNavView;

    //region Card 1
    private CardView addCard1;

    private EditText itemNameTextField;
    private TextView itemNameErrorText;

    private EditText itemUPCTextField;
    private TextView itemUPCErrorText;

    private TextView itemTypeCommonTwoWayButton;
    private TextView itemTypeBrandedTwoWayButton;
    private ImageView itemTypeWayVisual;

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

    private EditText itemCalorieTextField;
    private TextView itemCalorieErrorText;

    private EditText itemProteinTextField;
    private TextView itemProteinErrorText;

    private EditText itemCarbTextField;
    private TextView itemCarbErrorText;

    private EditText itemFatTextField;
    private TextView itemFatErrorText;

    private LinearLayout secondCarouselBackButton;
    //endregion

    private boolean isCommon = true;

    private Button addButton;

    private String incUpcid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);
        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);

        addCard1 = findViewById(R.id.addItemCard1);
        itemNameTextField = findViewById(R.id.itemNameTextField);
        itemNameErrorText = findViewById(R.id.nameErrorLabelText);
        itemUPCTextField = findViewById(R.id.upcTextField);
        itemUPCErrorText = findViewById(R.id.upcErrorLabelText);

        itemTypeCommonTwoWayButton = findViewById(R.id.commonButton);
        itemTypeBrandedTwoWayButton = findViewById(R.id.brandedButton);
        itemTypeWayVisual = findViewById(R.id.itemTypeTwoWayVisual);

        itemBrandLayout = findViewById(R.id.brandNameLayout);
        itemBrandTextField = findViewById(R.id.brandTextField);
        itemBrandErrorText = findViewById(R.id.brandErrorLabelText);

        addCard2 = findViewById(R.id.addCard2);
        servingAmtTextField = findViewById(R.id.servingAmtTextField);
        servingSizeTextField = findViewById(R.id.servingSizeTextField);
        servingErrorTextField = findViewById(R.id.servingErrorLabelText);

        itemCalorieTextField = findViewById(R.id.calorieTextField);
        itemCalorieErrorText = findViewById(R.id.caloriesErrorLabelText);

        itemProteinTextField = findViewById(R.id.proteinTextField);
        itemProteinErrorText = findViewById(R.id.proteinErrorLabelText);

        itemCarbTextField = findViewById(R.id.carbsTextField);
        itemCarbErrorText = findViewById(R.id.carbsErrorLabelText);

        itemFatTextField = findViewById(R.id.fatTextField);
        itemFatErrorText = findViewById(R.id.fatErrorLabelText);

        addButton = findViewById(R.id.addBtn);

        firstCarouselNextButton = findViewById(R.id.carouselForwardFirstButton);
        secondCarouselBackButton = findViewById(R.id.carouselBackSecondButton);

        itemNameTextField.setOnFocusChangeListener(this);
        itemUPCTextField.setOnFocusChangeListener(this);
        itemBrandTextField.setOnFocusChangeListener(this);

        servingAmtTextField.setOnFocusChangeListener(this);
        servingSizeTextField.setOnFocusChangeListener(this);

        addButton.setOnClickListener(this);

        itemTypeBrandedTwoWayButton.setOnClickListener(this);
        itemTypeCommonTwoWayButton.setOnClickListener(this);

        firstCarouselNextButton.setOnClickListener(this);
        secondCarouselBackButton.setOnClickListener(this);


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
        }

        if (id == itemTypeCommonTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, itemTypeCommonTwoWayButton, itemTypeBrandedTwoWayButton, itemTypeWayVisual, 370, true,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(true);
        } else if (id == itemTypeBrandedTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, itemTypeCommonTwoWayButton, itemTypeBrandedTwoWayButton, itemTypeWayVisual, 370, false,  R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            setBrandField(false);
        }

        if (id == firstCarouselNextButton.getId()) {
            setActiveCard(2);
        } else if (id == secondCarouselBackButton.getId()) {
            setActiveCard(1);
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
    public void onFocusChange(View v, boolean hasFocus) {
        //TODO: ux for fields
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

        switch (card) {
            case 1:
                addCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                addCard2.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void addItem(@Nullable EventCallback callback) {
        //TODO: give errors if not filled out, make form more advanced for adding vitamins and types of, perhaps by scanning nutrition label
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

        if (itemName.isEmpty() || itemUpc.isEmpty() || servingAmt.isEmpty() || servingSize.isEmpty()) {
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());
            return;
        }
        if (!isCommon && brandName.isEmpty()){
            callback.onFailure(new EventContext.Builder().withError("Empty fields").build());
            return;
        }

        calories = !calories.isEmpty() ? calories : "0";
        protein = !protein.isEmpty() ? protein : "0";
        carbs = !carbs.isEmpty() ? carbs : "0";
        fat = !fat.isEmpty() ? fat : "0";

        //TODO: check duplicate to see if it already exists in advanced setting

        HashMap<Nutrient, Double> newItemNutrients = new HashMap<>();
        newItemNutrients.put(Nutrient.Calorie, Double.valueOf(calories));
        newItemNutrients.put(Nutrient.Protein, Double.valueOf(protein));
        newItemNutrients.put(Nutrient.TotalCarb, Double.valueOf(carbs));
        newItemNutrients.put(Nutrient.TotalFat, Double.valueOf(fat));

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
}