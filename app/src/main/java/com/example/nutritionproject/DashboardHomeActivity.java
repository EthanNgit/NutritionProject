package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.UI.MealListAdapter;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityAddFoodItemBinding;
import com.example.nutritionproject.databinding.ActivityDashboardHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class DashboardHomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{
    private final CustomDBMethods dbManager = new CustomDBMethods();
    private ActivityDashboardHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    public void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        dbManager.onUserCurrentNutritionUpdateSuccess.addListener(this, "checkCalorieProgress");
        UserProfileStaticRefOther.onUserMealHistoryUpdate.addListener(this, "updateMealHistory");

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.emptyAddMealBtn.setOnClickListener(this);
        binding.addMealBtn.setOnClickListener(this);
        binding.profileBtn.setOnClickListener(this);
        binding.tdeeBtn.setOnClickListener(this);

        setHomeWidgets();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.profileBtn.getId())
        {
            startActivity(new Intent(DashboardHomeActivity.this, ProfileActivity.class));
        }
        if (id == binding.tdeeBtn.getId())
        {
            startActivity(new Intent(DashboardHomeActivity.this, TdeeActivity.class));
        }
        if (id == binding.addMealBtn.getId() || id == binding.emptyAddMealBtn.getId())
        {
            //TODO: Bring to add meal screen, search screen, or something.
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, binding.bottomNavigationView, item);

        return false;
    }

    private void setHomeWidgets()
    {
        getFirstTimeData();

        updateMealHistory();

        checkCalorieProgress();

        setProfileButton();
    }

    private void updateMealRecyclerView(ArrayList<MealProfile> meals)
    {
        if (meals.size() > 0)
        {
            RecyclerViewInterface recyclerViewInterface = this;
            MealListAdapter adapter = new MealListAdapter(this, meals, recyclerViewInterface);

            binding.mealPlannerRecyclerView.setAdapter(adapter);
            binding.mealPlannerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void getFirstTimeData()
    {
        dbManager.updateNutrition(CurrentProfile.id, 0, 0, 0, 0,
                UserProfileStaticRefOther.userMealHistory, String.valueOf(new LocalDate()), null, new EventCallback()
        {
            @Override
            public void onSuccess(@Nullable EventContext context)
            {
                checkMeals(context);
            }

            @Override
            public void onFailure(@Nullable EventContext context)
            {
                checkMeals(context);
            }
        });
    }

    public void checkMeals(EventContext context)
    {
        try
        {
            ArrayList<MealProfile> meals = (ArrayList<MealProfile>) context.getData();

            if (!meals.isEmpty())
            {
                binding.mealPlannerRecyclerView.setVisibility(View.VISIBLE);
                binding.emptyMealPlannerLayout.setVisibility(View.GONE);
            }

            updateMealRecyclerView(meals);
            checkCalorieProgress();
        }
        catch (NullPointerException e)
        {
            binding.emptyMealPlannerLayout.setVisibility(View.VISIBLE);
            binding.mealPlannerRecyclerView.setVisibility(View.GONE);
        }
    }

    public void updateMealHistory()
    {
        binding.emptyMealPlannerLayout.setVisibility(View.VISIBLE);
        binding.mealPlannerRecyclerView.setVisibility(View.GONE);

        updateMealRecyclerView(UserProfileStaticRefOther.userMealHistory);
    }

    public void checkCalorieProgress()
    {
        if (CurrentProfile.goals.calories == 0)
        {
            binding.firstTimeCalSetCard.setVisibility(View.VISIBLE);
            binding.calProgressChartCard.setVisibility(View.GONE);
        }
        else
        {
            binding.firstTimeCalSetCard.setVisibility(View.GONE);
            binding.calProgressChartCard.setVisibility(View.VISIBLE);

            double calPercent = ((double) CurrentProfile.currentMacros.calories / (double) CurrentProfile.goals.calories) * 100.0;
            binding.calProgressBar.setPercent((int)calPercent);

            SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.calories, R.color.darkTheme_Brand, R.color.darkTheme_WhiteMed);
            binding.currentGoalCalorieLabel.setText("of " + String.valueOf(CurrentProfile.goals.calories) + " kcal");
            binding.currentCalorieLabel.setText(curCalBuilder, TextView.BufferType.SPANNABLE);

            SpannableStringBuilder curProBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.proteins, R.color.darkTheme_Protein, R.color.darkTheme_WhiteMed);
            binding.proteinValue.setText(curProBuilder, TextView.BufferType.SPANNABLE);
            binding.totalProteinValue.setText("of " + String.valueOf(CurrentProfile.goals.proteins) + " protein");

            SpannableStringBuilder curCarBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.carbs, R.color.darkTheme_Carb, R.color.darkTheme_WhiteMed);
            binding.totalCarbValue.setText("of " + String.valueOf(CurrentProfile.goals.carbs) + " carbs");
            binding.carbValue.setText(curCarBuilder, TextView.BufferType.SPANNABLE);

            SpannableStringBuilder curFatBuilder = CustomUIMethods.getMultiColouredMacroText(this, CurrentProfile.currentMacros.fats, R.color.darkTheme_Fat, R.color.darkTheme_WhiteMed);
            binding.totalFatValue.setText("of " + String.valueOf(CurrentProfile.goals.fats) + " fats");
            binding.fatValue.setText(curFatBuilder, TextView.BufferType.SPANNABLE);
        }
    }

    private void setProfileButton()
    {
        CustomUIMethods.setProfileButton(this, binding.profileBtn, CurrentProfile.userColorHex, binding.profileBtnText, CurrentProfile.email);
    }

    @Override
    public void onItemClick(int position)
    {

    }
}