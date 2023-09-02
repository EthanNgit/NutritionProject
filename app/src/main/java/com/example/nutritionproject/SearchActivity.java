package com.example.nutritionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nutritionproject.Custom.java.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavView;

    private final CustomUIMethods uiManager = new CustomUIMethods();

    private Event onSearchUpdated = new Event();

    private EditText searchField;
    private ImageView backBtn;
    private ImageView barcodeBtn;

    private LinearLayout emptySearchLayout;

    private CardView settingsPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        onSearchUpdated.addListener(this, "updateSearchResults");

        bottomNavView = findViewById(R.id.bottomNavigationView);

        settingsPanel = findViewById(R.id.searchFilterSettingsCard);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.searchBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        searchField = findViewById(R.id.searchTextField);
        backBtn = findViewById(R.id.backButton);
        barcodeBtn = findViewById(R.id.barcodeScanButton);

        emptySearchLayout = findViewById(R.id.emptySearchLayout);

        uiManager.showKeyboard(this, searchField);

        backBtn.setOnClickListener(this);
        barcodeBtn.setOnClickListener(this);
        searchField.addTextChangedListener(this);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        } else if (id == barcodeBtn.getId()) {
            startActivity(new Intent(SearchActivity.this, SearchScanActivity.class));
        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        onSearchUpdated.invoke();
    }

    public void updateSearchResults() {
        //TODO: Implement searching

        // if search results found
        // show them hide layout
        // else
        // show no result empty layout
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        uiManager.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }


}