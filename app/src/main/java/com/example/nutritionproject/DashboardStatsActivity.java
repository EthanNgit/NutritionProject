package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomStatsMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Custom.SingleRowCalendar;
import com.example.nutritionproject.Custom.java.Custom.UI.RecyclerViewInterface;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.Widget;
import com.example.nutritionproject.Custom.java.Custom.UI.Widget.WidgetObject;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.HistoryModel.HistoryProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.Custom.java.UserModel.UserMacros;
import com.example.nutritionproject.Custom.java.UserModel.UserProfileStaticRefOther;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityDashboardStatsBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.progressview.ProgressView;

import org.eazegraph.lib.models.PieModel;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import kotlin.Triple;

public class DashboardStatsActivity extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener, RecyclerViewInterface
{
    CustomDBMethods dbManager = new CustomDBMethods();
    private ArrayList<WidgetObject> allWidgets;
    private HashMap<Widget, View> widgetIdToViewMap = new HashMap<>();
    private HashMap<String, ProgressView> weekdayToCalorieViewMap = new HashMap<>();
    private HashMap<String, Triple<ProgressView, ProgressView, ProgressView>> weekdayToMacroViewMap = new HashMap<>();
    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();
    private ArrayList<HistoryProfile> savedHistories = new ArrayList<>();
    private SingleRowCalendar calendar;
    private String currentDate;
    Gson gson = new Gson();
    private ActivityDashboardStatsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardStatsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        setWidgetPreferences();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.statsBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.settingsBtn.setOnClickListener(this);
        binding.dropdownBtn.setOnClickListener(this);

        calendar = new SingleRowCalendar(binding.mainSingleRowCalendar, binding.monthLabel, binding.yearLabel, binding.leftBtn, binding.rightBtn, new EventCallback()
        {
            @Override
            public void onSuccess(@Nullable EventContext context)
            {
                Date date = (Date) context.getData();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = formatter.format(date);
                currentDate = formattedDate;

                for (HistoryProfile historyProfile: savedHistories)
                {
                    handleWidget();
                    if (historyProfile.getDate().equals(formattedDate)) return;
                }

                dbManager.getHistoryOfWeekFrom(CurrentProfile.id, formattedDate, new EventCallback()
                {
                    @Override
                    public void onSuccess(@Nullable EventContext context)
                    {
                        savedHistories = (ArrayList<HistoryProfile>) context.getData();

                        Date date = new Date();
                        String today = formatter.format(date);
                        UserMacros currentMacros = CurrentProfile.currentMacros;
                        UserMacros goalMacros = CurrentProfile.goals;

                        Log.d("NORTH_HERE", savedHistories.size() + "");

                        if (savedHistories.size() == 0)
                        {
                            handleWidget();
                            return;
                        }

                        if (savedHistories.size() < 7);
                        {
                            savedHistories.add(new HistoryProfile(goalMacros.calories, goalMacros.proteins, goalMacros.carbs, goalMacros.fats,
                                    goalMacros.alcohol, currentMacros.calories, currentMacros.proteins, currentMacros.carbs, currentMacros.fats, currentMacros.alcohol,
                                    UserProfileStaticRefOther.userMealHistory, today, savedHistories.get(savedHistories.size() - 1).getStreak() + 1));

                            Log.d("North", savedHistories.get(savedHistories.size() - 1).getStreak() + " ");
                        }

                        handleWidget();
                    }

                    @Override
                    public void onFailure(@Nullable EventContext context)
                    {
                        savedHistories.clear();
                        handleWidget();
                    }
                });
            }

            @Override
            public void onFailure(@Nullable EventContext context)
            {

            }
        });

        widgetIdToViewMap.put(Widget.Calorie, binding.calorieWidget);
        widgetIdToViewMap.put(Widget.Macros, binding.macrosWidget);
        //widgetIdToViewMap.put(Widget.Weight, binding.weightWidget);
        widgetIdToViewMap.put(Widget.Goals, binding.goalsWidget);
        widgetIdToViewMap.put(Widget.Streaks, binding.streaksWidget);
        widgetIdToViewMap.put(Widget.Nutrition, binding.nutritionWidget);

        handleWidget();
    }

    private void handleWidget()
    {
        if (allWidgets == null)
        {
            // Check preferences for a saved layout
            getWidgetPreferences();
        }

        // Create default layout

        for (WidgetObject widget: allWidgets)
        {
            int isVisible = widget.isEnabled ? View.VISIBLE : View.GONE;

            widgetIdToViewMap.get(widget.widgetId).setVisibility(isVisible);

            if (widget.isEnabled)
            {
                switch (widget.widgetId)
                {
                    case Calorie:
                        updateCalorieWidget();
                        break;
                    case Macros:
                        updateMacrosWidget();
                        break;
                    //case Weight:
                    //    updateWeightWidget();
                    //    break;
                    case Goals:
                        updateGoalsWidget();
                        break;
                    case Streaks:
                        updateStreaksWidget();
                        break;
                    case Nutrition:
                        updateNutritionWidget();
                        break;
                }
            }
        }
    }

    private void getWidgetPreferences()
    {
        SharedPreferences preferences = getSharedPreferences("stats", MODE_PRIVATE);
        String jsonString = preferences.getString("stats_widgets", null);
        Type listType = new TypeToken<ArrayList<WidgetObject>>() {}.getType();

        if (jsonString != null)
        {
            Log.d("NORTH", "Preferences found");
            allWidgets = gson.fromJson(jsonString, listType);
        }
        else
        {
            Log.d("NORTH", "no preferences found");

            WidgetObject caloriesWidget = new WidgetObject(Widget.Calorie, "Calorie Widget",
                    R.drawable.ic_fire, R.color.widget_fire, true);
            WidgetObject macrosWidget = new WidgetObject(Widget.Macros, "Macros Widget",
                    R.drawable.ic_fire, R.color.darkTheme_Brand, true);
            //WidgetObject weightWidget = new WidgetObject(Widget.Weight, "Weight Widget",
            //        R.drawable.ic_weight, R.color.widget_metal, true);
            WidgetObject goalsWidget = new WidgetObject(Widget.Goals, "Goals Widget",
                    R.drawable.ic_goals, R.color.widget_goal, false);
            WidgetObject streaksWidget = new WidgetObject(Widget.Streaks, "Streaks Widget",
                    R.drawable.ic_streak, R.color.widget_streak, true);
            WidgetObject nutritionWidget = new WidgetObject(Widget.Nutrition, "Nutrition Widget",
                    R.drawable.ic_fire, R.color.darkTheme_Brand, false);

            allWidgets = new ArrayList<>();

            allWidgets.add(caloriesWidget);
            allWidgets.add(macrosWidget);
            //allWidgets.add(weightWidget);
            allWidgets.add(goalsWidget);
            allWidgets.add(streaksWidget);
            allWidgets.add(nutritionWidget);
        }

        setWidgetPreferences();
    }

    private void setWidgetPreferences()
    {
        Log.d("NORTH", "PREFERENCES SAVED");

        SharedPreferences preferences = getSharedPreferences("stats", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("stats_widgets", gson.toJson(allWidgets));
        editor.apply();
    }

    private void updateCalorieWidget()
    {
        if (weekdayToCalorieViewMap.isEmpty())
        {
            weekdayToCalorieViewMap.put("Monday", binding.calorieWidgetProgressBar1);
            weekdayToCalorieViewMap.put("Tuesday", binding.calorieWidgetProgressBar2);
            weekdayToCalorieViewMap.put("Wednesday", binding.calorieWidgetProgressBar3);
            weekdayToCalorieViewMap.put("Thursday", binding.calorieWidgetProgressBar4);
            weekdayToCalorieViewMap.put("Friday", binding.calorieWidgetProgressBar5);
            weekdayToCalorieViewMap.put("Saturday", binding.calorieWidgetProgressBar6);
            weekdayToCalorieViewMap.put("Sunday", binding.calorieWidgetProgressBar7);
        }

        clearCalorieWidget();

        double sumOfCalories = 0;

        for (HistoryProfile profile: savedHistories)
        {
            sumOfCalories += profile.getEndCalorie();

            if (profile.getCalorie() == 0.0)
            {
                continue;
            }

            if (profile.getDate().equals(currentDate))
            {
                double calPercent = (profile.getEndCalorie() / profile.getCalorie()) * 100.0;
                boolean shouldUseErrorColor = false;

                if (calPercent > 100.0)
                {
                    calPercent = 100.0;
                    shouldUseErrorColor = true;
                    binding.calorieWidgetProgressBar0.setProgressBarColor(ContextCompat.getColor(this, R.color.darkTheme_Calorie_Error));
                }
                else
                {
                    binding.calorieWidgetProgressBar0.setProgressBarColor(ContextCompat.getColor(this, R.color.darkTheme_Brand));
                }

                binding.calorieWidgetProgressBar0.setPercentWithAnimation((int)calPercent);

                SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, (int) profile.getEndCalorie(), null, shouldUseErrorColor? R.color.darkTheme_Calorie_Error : R.color.darkTheme_Brand, R.color.darkTheme_WhiteMed);
                binding.calorieWidgetValueText.setText(curCalBuilder, TextView.BufferType.SPANNABLE);
            }

            String weekday = LocalDate.parse(profile.getDate()).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            float fillPercent = (float)(profile.getEndCalorie() / profile.getCalorie());

            if (fillPercent > 1)
            {
                weekdayToCalorieViewMap.get(weekday).getHighlightView().setColor(ContextCompat.getColor(this, R.color.darkTheme_Calorie_Error));
            } else {
                weekdayToCalorieViewMap.get(weekday).getHighlightView().setColor(ContextCompat.getColor(this, R.color.darkTheme_Brand));
            }

            weekdayToCalorieViewMap.get(weekday).setProgress(fillPercent);
        }

        if (savedHistories.isEmpty())
        {
            clearCalorieWidget();
        }
        else
        {
            int average = (int)(sumOfCalories / 7);
            binding.calorieWidgetAverageIntakeText.setText(average + "kcal average per day");
        }
    }
    private void clearCalorieWidget()
    {
        binding.calorieWidgetProgressBar0.setPercentWithAnimation(0);

        SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, (int) 0, null,  R.color.darkTheme_Brand, R.color.darkTheme_WhiteMed);
        binding.calorieWidgetValueText.setText(curCalBuilder, TextView.BufferType.SPANNABLE);

        for (ProgressView view : weekdayToCalorieViewMap.values())
        {
            view.setProgress(0);
        }

        binding.calorieWidgetAverageIntakeText.setText(R.string.widget_no_data);
    }

    private void updateMacrosWidget()
    {
        if (weekdayToMacroViewMap.isEmpty())
        {
            weekdayToMacroViewMap.put("Monday", new Triple<>(binding.macroWidgetProgressBar11, binding.macroWidgetProgressBar12, binding.macroWidgetProgressBar13));
            weekdayToMacroViewMap.put("Tuesday", new Triple<>(binding.macroWidgetProgressBar21, binding.macroWidgetProgressBar22, binding.macroWidgetProgressBar23));
            weekdayToMacroViewMap.put("Wednesday", new Triple<>(binding.macroWidgetProgressBar31, binding.macroWidgetProgressBar32, binding.macroWidgetProgressBar33));
            weekdayToMacroViewMap.put("Thursday", new Triple<>(binding.macroWidgetProgressBar41, binding.macroWidgetProgressBar42, binding.macroWidgetProgressBar43));
            weekdayToMacroViewMap.put("Friday", new Triple<>(binding.macroWidgetProgressBar51, binding.macroWidgetProgressBar52, binding.macroWidgetProgressBar53));
            weekdayToMacroViewMap.put("Saturday", new Triple<>(binding.macroWidgetProgressBar61, binding.macroWidgetProgressBar62, binding.macroWidgetProgressBar63));
            weekdayToMacroViewMap.put("Sunday", new Triple<>(binding.macroWidgetProgressBar71, binding.macroWidgetProgressBar72, binding.macroWidgetProgressBar73));
        }

        clearMacrosWidget();

        double sumOfCalories = 0;
        double sumOfProtein = 0;
        double sumOfCarb = 0;
        double sumOfFat = 0;

        for (HistoryProfile profile: savedHistories)
        {
            sumOfCalories += profile.getEndCalorie();
            sumOfProtein += profile.getEndProtein() * 4;
            sumOfCarb += profile.getEndCarb() * 4;
            sumOfFat += profile.getEndFat() * 9;

            if (profile.getCalorie() == 0)
            {
                continue;
            }

            if (profile.getDate().equals(currentDate))
            {
                int proteinAmount = (int) profile.getEndProtein();
                int carbsAmount = (int) profile.getEndCarb();
                int fatsAmount = (int) profile.getEndFat();

                if (proteinAmount == 0 && carbsAmount == 0 && fatsAmount == 0)
                {
                    CustomStatsMethods.clearPieChart(this, binding.macroWidgetPieChart, R.color.darkTheme_Background);
                }
                else
                {
                    int proteinColor = R.color.darkTheme_Protein;
                    int carbsColor = R.color.darkTheme_Carb;
                    int fatsColor = R.color.darkTheme_Fat;

                    String[] labels = {"Protein", "Carbs", "Fats"};
                    int[] values = {proteinAmount * 4, carbsAmount * 4, fatsAmount * 9};
                    int[] colors = {proteinColor, carbsColor, fatsColor};

                    CustomStatsMethods.fillPieChart(this, binding.macroWidgetPieChart, labels, values, colors);
                }
            }

            String weekday = LocalDate.parse(profile.getDate()).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            double totalCalories = (profile.getEndProtein() * 4) + (profile.getEndCarb() * 4) + (profile.getEndFat() * 9);

            float proteinFillPercent = (float)((profile.getEndProtein() * 4) / totalCalories);
            float carbFillPercent = (float)((profile.getEndCarb() * 4) / totalCalories);
            float fatFillPercent = (float)((profile.getEndFat() * 9) / totalCalories);

            proteinFillPercent += proteinFillPercent != 0 ? carbFillPercent + fatFillPercent : 0;
            carbFillPercent += carbFillPercent != 0 ?  fatFillPercent : 0;

            weekdayToMacroViewMap.get(weekday).component1().setProgress(proteinFillPercent);
            weekdayToMacroViewMap.get(weekday).component2().setProgress(carbFillPercent);
            weekdayToMacroViewMap.get(weekday).component3().setProgress(fatFillPercent);
        }

        if (savedHistories.isEmpty())
        {
            clearMacrosWidget();
        }
        else
        {
            int proteinAvg = (int)((sumOfProtein / sumOfCalories) * 100);
            int carbAvg = (int)((sumOfCarb / sumOfCalories) * 100);
            int fatAvg = (int)((sumOfFat / sumOfCalories) * 100);

            SpannableStringBuilder builder = new SpannableStringBuilder();

            String proteinPercent = String.valueOf(proteinAvg) + "%  ";
            SpannableString proteinSpannable= new SpannableString(proteinPercent);
            proteinSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.darkTheme_Protein)), 0, proteinPercent.length(), 0);
            builder.append(proteinSpannable);

            String carbPercent = String.valueOf(carbAvg) + "%  ";
            SpannableString carbSpannable= new SpannableString(carbPercent);
            carbSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.darkTheme_Carb)), 0, carbPercent.length(), 0);
            builder.append(carbSpannable);

            String fatPercent = String.valueOf(fatAvg) + "%  ";
            SpannableString fatSpannable= new SpannableString(fatPercent);
            fatSpannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.darkTheme_Fat)), 0, fatPercent.length(), 0);
            builder.append(fatSpannable);

            binding.macroWidgetAverageRatios.setText(builder, TextView.BufferType.SPANNABLE);
        }
    }

    private void clearMacrosWidget()
    {
        CustomStatsMethods.clearPieChart(this, binding.macroWidgetPieChart, R.color.darkTheme_Background);

        for (Triple<ProgressView, ProgressView, ProgressView> triplet: weekdayToMacroViewMap.values())
        {
            triplet.component1().setProgress(0);
            triplet.component2().setProgress(0);
            triplet.component3().setProgress(0);
        }

        binding.macroWidgetAverageRatios.setText(R.string.widget_no_data);
    }

    private void updateWeightWidget()
    {
        // Get the last months worth of data and calculate
        // weight lost or gained by comparing it to their tdee
    }
    private void updateGoalsWidget()
    {
        CustomStatsMethods.clearPieChart(this, binding.macroPieChart, R.color.darkTheme_Background);

        binding.goalWidgetCalorieValue.setText("0 Calories");
        binding.goalWidgetProteinValue.setText("0 Protein");
        binding.goalWidgetCarbValue.setText("0 Carbs");
        binding.goalWidgetFatValue.setText("0 Fats");

        for (HistoryProfile profile: savedHistories)
        {
            if (profile.getDate().equals(currentDate))
            {
                CustomStatsMethods.clearPieChart(this, binding.macroPieChart, R.color.darkTheme_Background);

                int calorieGoal = (int) profile.getCalorie();
                int proteinGoal = (int) profile.getProtein();
                int carbsGoal = (int) profile.getCarb();
                int fatsGoal = (int) profile.getFat();

                int calorieColor = R.color.darkTheme_Brand;
                int proteinColor = R.color.darkTheme_Protein;
                int carbsColor = R.color.darkTheme_Carb;
                int fatsColor = R.color.darkTheme_Fat;

                String[] labels = {"Protein", "Carbs", "Fats"};
                int[] values = {proteinGoal * 4, carbsGoal * 4, fatsGoal * 9};
                int[] colors = {proteinColor, carbsColor, fatsColor};

                CustomStatsMethods.fillPieChart(this, binding.macroPieChart, labels, values, colors);

                SpannableStringBuilder curCalBuilder = CustomUIMethods.getMultiColouredMacroText(this, calorieGoal, " Calories", calorieColor, R.color.darkTheme_WhiteMed);
                binding.goalWidgetCalorieValue.setText(curCalBuilder, TextView.BufferType.SPANNABLE);

                SpannableStringBuilder curProBuilder = CustomUIMethods.getMultiColouredMacroText(this, proteinGoal, "g Protein", proteinColor, R.color.darkTheme_WhiteMed);
                binding.goalWidgetProteinValue.setText(curProBuilder, TextView.BufferType.SPANNABLE);

                SpannableStringBuilder curCarBuilder = CustomUIMethods.getMultiColouredMacroText(this, carbsGoal, "g Carbs", carbsColor, R.color.darkTheme_WhiteMed);
                binding.goalWidgetCarbValue.setText(curCarBuilder, TextView.BufferType.SPANNABLE);

                SpannableStringBuilder curFatBuilder = CustomUIMethods.getMultiColouredMacroText(this, fatsGoal, "g Fats", fatsColor, R.color.darkTheme_WhiteMed);
                binding.goalWidgetFatValue.setText(curFatBuilder, TextView.BufferType.SPANNABLE);
            }
        }
    }
    private void updateStreaksWidget()
    {
        binding.loginStreakText.setText("0");

        if (!savedHistories.isEmpty())
        {
            for (HistoryProfile profile: savedHistories)
            {
                if (profile.getDate().equals(currentDate))
                {
                    binding.loginStreakText.setText(profile.getStreak() + "");
                }
            }
        }
    }
    private void updateNutritionWidget()
    {
        // Get all food from the day and make a nutrition label for it
        if (nutrientToTextMap.isEmpty())
        {
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
        }

        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> totalNutrients = new HashMap<>();

        for (HistoryProfile profile: savedHistories)
        {
            if (profile.getDate().equals(currentDate))
            {
                if (profile.getMeals() != null)
                {
                    for (MealProfile mealProfile: profile.getMeals())
                    {
                        for (FoodProfile foodProfile: mealProfile.mealComposition)
                        {
                            for (Nutrient nutrient : foodProfile.nutrition.nutrients.keySet())
                            {
                                if (totalNutrients.get(nutrient) != null)
                                {
                                    // add to the value
                                    Pair<Double, NutrientMeasurement> oldPair = foodProfile.nutrition.nutrients.get(nutrient);
                                    double newValue = oldPair.first + totalNutrients.get(nutrient).first;

                                    Pair<Double, NutrientMeasurement> newPair = new Pair<Double, NutrientMeasurement>(newValue, oldPair.second);

                                    totalNutrients.put(nutrient, newPair);
                                }
                                else
                                {
                                    totalNutrients.put(nutrient, foodProfile.nutrition.nutrients.get(nutrient));
                                }
                            }
                        }
                    }
                }
            }

        }

        if (!totalNutrients.isEmpty())
        {
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

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->
            {
                String jsonString = result.getData().getStringExtra("widgets");
                Type listType = new TypeToken<ArrayList<WidgetObject>>() {}.getType();
                if (jsonString != null)
                {
                    allWidgets = gson.fromJson(jsonString, listType);
                    setWidgetPreferences();
                    handleWidget();
                }
            });

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.settingsBtn.getId())
        {
            Intent intent = new Intent(DashboardStatsActivity.this, StatsEditWidgetActivity.class);
            intent.putExtra("widgets", gson.toJson(allWidgets));

            activityResultLauncher.launch(intent);
        }

        if (id == binding.dropdownBtn.getId())
        {
            CustomUIMethods.toggleDropdown(this, binding.dropdownIcon, binding.mealIngredientNutritionFacts);
        }
    }

    @Override
    public void onItemClick(int clickId, int position)
    {

    }
}