package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.MealListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Enums.ActivityResultCodes;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.databinding.ActivityDashboardHomeBinding;
import com.example.nutritionproject.databinding.ActivityDashboardSearchBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DashboardSearchActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, View.OnClickListener, RecyclerViewInterface
{
    ArrayList<MealProfile> mealProfiles = new ArrayList<>();
    private ActivityDashboardSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardSearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    public void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.searchBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.searchBtn.setOnClickListener(this);

        getRecentMealHistory();
    }

    private void getRecentMealHistory()
    {
        SharedPreferences preferences = getSharedPreferences(CurrentProfile.id+"_Meals", MODE_PRIVATE);
        String jsonString = preferences.getString("recent_meals", null);
        Type listType = new TypeToken<ArrayList<MealProfile>>() {}.getType();
        Gson gson = new Gson();

        if (jsonString != null)
        {
            binding.recentRecipesLabel.setVisibility(View.VISIBLE);
            binding.recentRecipeCard.setVisibility(View.VISIBLE);

            mealProfiles = gson.fromJson(jsonString, listType);

            if (mealProfiles.size() > 0)
            {
                RecyclerViewInterface recyclerViewInterface = this;
                MealListAdapter adapter = new MealListAdapter(this, mealProfiles, recyclerViewInterface);

                binding.recentRecipeRecyclerView.setAdapter(adapter);
                binding.recentRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
        else {
            binding.recentRecipesLabel.setVisibility(View.GONE);
            binding.recentRecipeCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.searchBtn.getId())
        {
            startActivity(new Intent(DashboardSearchActivity.this, SearchActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onItemClick(int clickId, int position)
    {
        MealProfile meal = mealProfiles.get(position);
        Gson gson = new Gson();
        String mealJson = gson.toJson(meal);

        Intent intent = new Intent(DashboardSearchActivity.this, MealItemViewActivity.class);
        intent.putExtra("mealJson", mealJson);
        intent.putExtra("add", true);

        startActivity(intent);
    }
}