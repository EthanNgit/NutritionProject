package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUtilityMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.FoodListAdapter;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.Model.FoodModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    //TODO: I am not sure if I exactly like this setup, but i will stick with it till I feel need to add Callbacks and Parameters to events

    private final CustomDBMethods dbManager = new CustomDBMethods();

    private BottomNavigationView bottomNavView;

    private EditText searchField;
    private ImageView backBtn;
    private ImageView barcodeBtn;
    private CardView addItemBtn;

    private LinearLayout emptySearchLayout;
    private RecyclerView fullSearchLayout;

    private CardView settingsPanel;
    private static String passedUpcId = "";
    private static Event updateSearchEvent = new Event();

    public static void setPassedUpcId(String upcId) {
        passedUpcId = upcId;
        updateSearchEvent.invoke();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        updateSearchEvent.addListener(this, "updateSearchResults");

        bottomNavView = findViewById(R.id.bottomNavigationView);

        settingsPanel = findViewById(R.id.searchFilterSettingsCard);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        searchField = findViewById(R.id.searchTextField);
        backBtn = findViewById(R.id.backButton);
        barcodeBtn = findViewById(R.id.barcodeScanButton);
        addItemBtn = findViewById(R.id.addBtn);

        emptySearchLayout = findViewById(R.id.emptySearchLayout);
        fullSearchLayout = findViewById(R.id.searchRecyclerView);

        emptySearchLayout.setVisibility(View.VISIBLE);
        fullSearchLayout.setVisibility(View.GONE);

        CustomUIMethods.showKeyboard(this, searchField);

        backBtn.setOnClickListener(this);
        barcodeBtn.setOnClickListener(this);
        addItemBtn.setOnClickListener(this);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null || event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;

                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    updateSearchResults();
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        } else if (id == barcodeBtn.getId()) {
            startActivity(new Intent(SearchActivity.this, SearchScanActivity.class));
        } else if (id == addItemBtn.getId()) {
            startActivity(new Intent(SearchActivity.this, AddFoodItemActivity.class));
        }


    }


    public void updateSearchResults() {
        if (!passedUpcId.isEmpty()) { searchField.setText(passedUpcId); }

        if (searchField.getText().toString().isEmpty()) { return; }

        //TODO: Update a layout with information
        Context parentContext = this;
        String searchText = searchField.getText().toString().trim().toLowerCase();
        boolean shouldUseUpc = dbManager.isOnlyDigits(searchText) ? true : false;
        dbManager.searchFoodItem(shouldUseUpc ? searchText : "", shouldUseUpc ? "" : searchText, new EventCallback() {
            @Override
            public void onSuccess(@Nullable EventContext context) {
                emptySearchLayout.setVisibility(View.GONE);
                fullSearchLayout.setVisibility(View.VISIBLE);

                ArrayList<FoodProfile> profiles = (ArrayList<FoodProfile>) context.getData();

                if (profiles.size() > 0) {
                    FoodListAdapter adapter = new FoodListAdapter(parentContext, profiles);
                    fullSearchLayout.setAdapter(adapter);
                    fullSearchLayout.setLayoutManager(new LinearLayoutManager(parentContext));
                }
            }

            @Override
            public void onFailure(@Nullable EventContext context) {
                emptySearchLayout.setVisibility(View.VISIBLE);
                fullSearchLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }


}