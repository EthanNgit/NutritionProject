package com.example.nutritionproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.nutritionproject.Custom.java.Custom.UI.FoodListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Enums.ActivityResultCodes;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityProfileGoalsBinding;
import com.example.nutritionproject.databinding.ActivitySearchBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{

    private final CustomDBMethods dbManager = new CustomDBMethods();
    private static Event updateSearchEvent = new Event();
    private ArrayList<FoodProfile> profiles;
    private static String passedUpcId = "";
    boolean searchingForIngredient = false;
    private FoodProfile searchedIngredient;
    private ActivitySearchBinding binding;

    public static void setPassedUpcId(String upcId)
    {
        passedUpcId = upcId;
        updateSearchEvent.invoke();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        updateSearchEvent.addListener(this, "updateSearchResults");

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.searchBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.emptySearchLayout.setVisibility(View.VISIBLE);
        binding.searchRecyclerView.setVisibility(View.GONE);

        CustomUIMethods.showKeyboard(this, binding.searchTextField);

        binding.backBtn.setOnClickListener(this);
        binding.barcodeScanBtn.setOnClickListener(this);
        binding.addBtn.setOnClickListener(this);
        binding.searchTextField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        binding.searchTextField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) return false;

                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_NULL)
                {
                    updateSearchResults();
                }

                return false;
            }
        });

        Intent intent = this.getIntent();
        if (intent != null && intent.getBooleanExtra("searchForIngredient", false))
        {
            searchingForIngredient = getIntent().getBooleanExtra("searchForIngredient", false);
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
        else if (id == binding.barcodeScanBtn.getId())
        {
            startActivity(new Intent(SearchActivity.this, SearchScanActivity.class));
        }
        else if (id == binding.addBtn.getId())
        {
            startActivity(new Intent(SearchActivity.this, AddFoodItemActivity.class));
        }

    }


    public void updateSearchResults()
    {
        if (!passedUpcId.isEmpty()) binding.searchTextField.setText(passedUpcId);

        if (binding.searchTextField.getText().toString().length() <= 1) return;

        Context parentContext = this;
        String searchText = binding.searchTextField.getText().toString().trim().toLowerCase();
        boolean shouldUseUpc = dbManager.isOnlyDigits(searchText) ? true : false;

        RecyclerViewInterface recyclerViewInterface = this;
        dbManager.searchFoodItem(shouldUseUpc ? searchText : "", shouldUseUpc ? "" : searchText, new EventCallback() 
        {
            @Override
            public void onSuccess(@Nullable EventContext context) 
            {
                binding.emptySearchLayout.setVisibility(View.GONE);
                binding.searchRecyclerView.setVisibility(View.VISIBLE);

                profiles = (ArrayList<FoodProfile>) context.getData();

                if (profiles.size() > 0) 
                {
                    FoodListAdapter adapter = new FoodListAdapter(parentContext, profiles, recyclerViewInterface);
                    binding.searchRecyclerView.setAdapter(adapter);
                    binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(parentContext));
                }
            }

            @Override
            public void onFailure(@Nullable EventContext context) 
            {
                binding.emptySearchLayout.setVisibility(View.VISIBLE);
                binding.searchRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    ActivityResultLauncher<Intent> launchActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                if (result.getResultCode() == ActivityResultCodes.FoodItemView.getCode())
                {
                    Intent intent = result.getData();
                    setResult(ActivityResultCodes.SearchView.getCode(), intent);
                    finish();
                }
            });

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) 
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }


    @Override
    public void onItemClick(int position) 
    {
        if (profiles == null) return;

        FoodProfile profile = profiles.get(position);

        Intent intent = new Intent(this, FoodItemViewActivity.class);

        Gson gson = new Gson();

        intent.putExtra("name", profile.name);
        intent.putExtra("upcid", profile.upcId);
        intent.putExtra("dateadded", profile.dateAdded);
        intent.putExtra("tags", gson.toJson(profile.tags));
        intent.putExtra("common", profile.isCommon);
        intent.putExtra("brand", profile.brandName);
        intent.putExtra("verified", profile.isVerified);
        intent.putExtra("nutrition", gson.toJson(profile.nutrition));
        intent.putExtra("isIngredient", searchingForIngredient);

        if (searchingForIngredient)
        {
            launchActivityResultLauncher.launch(intent);
        }
        else {
            startActivity(intent);
        }
    }
}