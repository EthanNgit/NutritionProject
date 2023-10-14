package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getAverageCaloriesPerDayFromKgsPerWeek;
import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getAverageCaloriesPerDayFromLbsPerWeek;
import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getImperialHeight;
import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getImperialWeight;
import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getMacrosFromSplit;
import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Enums.WorkoutIntensity;
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityProfileGoalsBinding;
import com.example.nutritionproject.databinding.ActivityTdeeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TdeeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, NavigationBarView.OnItemSelectedListener
{
    private CustomFitMethods fitManager = new CustomFitMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();
    private WorkoutIntensity currentIntensity = WorkoutIntensity.Sedentary;
    public static Event onTDEEUserGoalUpdated = new Event();
    private Event onTDEEInformationChanged = new Event();
    private Map<TextView, ImageView> activityButtonToSelectViewMap = new HashMap<>();
    private Map<TextView, ImageView> splitButtonToSelectViewMap = new HashMap<>();
    private boolean isUpdatingManually = false;
    private boolean isMetric = true;
    private boolean isMale = true;
    private int[] currentSplit = new int[] {40, 30, 30};
    private int currentCalorieResult = 0;
    private ActivityTdeeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityTdeeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        binding.bottomNavigationView.setItemIconTintList(null);
        binding.bottomNavigationView.getMenu().findItem(R.id.homeBtn).setChecked(true);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.backBtn.setOnClickListener(this);

        onTDEEInformationChanged.addListener(this, "setCalorieField");

        activityButtonToSelectViewMap.put(binding.sedentaryExerciseBtn, binding.sedentaryExerciseViewer);
        activityButtonToSelectViewMap.put(binding.lightExerciseBtn, binding.lightExerciseViewer);
        activityButtonToSelectViewMap.put(binding.moderateExerciseBtn, binding.moderateExerciseViewer);
        activityButtonToSelectViewMap.put(binding.heavyExerciseBtn, binding.heavyExerciseViewer);
        activityButtonToSelectViewMap.put(binding.athleteBtn, binding.athleteViewer);

        splitButtonToSelectViewMap.put(binding.splitOneBtn, binding.splitOneViewer);
        splitButtonToSelectViewMap.put(binding.splitTwoBtn, binding.splitTwoViewer);
        splitButtonToSelectViewMap.put(binding.splitThreeBtn, binding.splitThreeViewer);

        binding.metricMeasurementBtn.setOnClickListener(this);
        binding.imperialMeasurementBtn.setOnClickListener(this);

        binding.maleGenderBtn.setOnClickListener(this);
        binding.femaleGenderBtn.setOnClickListener(this);

        binding.carouselForwardFirstBtn.setOnClickListener(this);
        binding.carouselBackSecondBtn.setOnClickListener(this);
        binding.carouselForwardSecondBtn.setOnClickListener(this);
        binding.carouselBackThirdBtn.setOnClickListener(this);

        binding.setGoalBtn.setOnClickListener(this);

        binding.setManualGoalBtn.setOnClickListener(this);
        binding.getAutomaticGoalBtn.setOnClickListener(this);

        for (TextView button : activityButtonToSelectViewMap.keySet())
        {
            button.setOnClickListener(this);
        }

        for (TextView button : splitButtonToSelectViewMap.keySet())
        {
            button.setOnClickListener(this);
        }

        binding.ageTextField.setOnFocusChangeListener(this);
        binding.poundsTextField.setOnFocusChangeListener(this);
        binding.kgTextField.setOnFocusChangeListener(this);
        binding.feetTextField.setOnFocusChangeListener(this);
        binding.inchTextField.setOnFocusChangeListener(this);
        binding.cmHeightField.setOnFocusChangeListener(this);

        binding.ageTextField.addTextChangedListener(this);
        binding.poundsTextField.addTextChangedListener(this);
        binding.kgTextField.addTextChangedListener(this);
        binding.feetTextField.addTextChangedListener(this);
        binding.inchTextField.addTextChangedListener(this);
        binding.cmHeightField.addTextChangedListener(this);

        binding.weightGoalTextField.addTextChangedListener(this);

        binding.calorieTextField.addTextChangedListener(this);
        binding.proteinTextField.addTextChangedListener(this);
        binding.carbsTextField.addTextChangedListener(this);
        binding.fatTextField.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
        }

        if (id == binding.metricMeasurementBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.metricMeasurementBtn, binding.imperialMeasurementBtn, binding.measurementTwoWayVisualOne,
                    binding.measurementTwoWayVisualTwo, true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);

            isMetric = true;
            setMetricFields();
        }
        else if (id == binding.imperialMeasurementBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.metricMeasurementBtn,  binding.imperialMeasurementBtn, binding.measurementTwoWayVisualOne,
                    binding.measurementTwoWayVisualTwo, false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);

            isMetric = false;
            setImperialFields();
        }

        if (id == binding.maleGenderBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.maleGenderBtn, binding.femaleGenderBtn, binding.genderTwoWayVisualOne, binding.genderTwoWayVisualTwo,
                    true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);

            isMale = true;
            onTDEEInformationChanged.invoke();
        }
        else if (id == binding.femaleGenderBtn.getId())
        {
            CustomUIMethods.setTwoWayButton(this, binding.maleGenderBtn, binding.femaleGenderBtn, binding.genderTwoWayVisualOne, binding.genderTwoWayVisualTwo,
                    false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);

            isMale = false;
            onTDEEInformationChanged.invoke();
        }

        if (id == binding.sedentaryExerciseBtn.getId())
        {
            setActivityLevel(binding.sedentaryExerciseBtn, WorkoutIntensity.Sedentary);
        }
        else if (id == binding.lightExerciseBtn.getId())
        {
            setActivityLevel(binding.lightExerciseBtn, WorkoutIntensity.Light);
        }
        else if (id == binding.moderateExerciseBtn.getId())
        {
            setActivityLevel(binding.moderateExerciseBtn, WorkoutIntensity.Moderate);
        }
        else if (id == binding.heavyExerciseBtn.getId())
        {
            setActivityLevel(binding.heavyExerciseBtn, WorkoutIntensity.Heavy);
        }
        else if (id == binding.athleteBtn.getId())
        {
            setActivityLevel(binding.athleteBtn, WorkoutIntensity.Athlete);
        }

        if (id == binding.splitOneBtn.getId())
        {
            setMacroSplit(binding.splitOneBtn,40, 30, 30);
        }
        else if (id == binding.splitTwoBtn.getId())
        {
            setMacroSplit(binding.splitTwoBtn,25, 30, 45);
        }
        else if (id == binding.splitThreeBtn.getId())
        {
            setMacroSplit(binding.splitThreeBtn,34, 33, 43);
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
        else if (id == binding.carouselBackThirdBtn.getId())
        {
            setActiveCard(2);
        }
        
        if (id == binding.setGoalBtn.getId())
        {
            setGoal();
        }
        else if (id == binding.setManualGoalBtn.getId())
        {
            closeCards();
            binding.calorieResultLabelText.setVisibility(View.GONE);

            binding.setManualGoalBtn.setVisibility(View.GONE);
            binding.manualTdeeCard.setVisibility(View.VISIBLE);
            binding.getAutomaticGoalBtn.setVisibility(View.VISIBLE);

            isUpdatingManually = true;
        }
        else if (id == binding.getAutomaticGoalBtn.getId())
        {
            closeCards();
            binding.calorieResultLabelText.setVisibility(View.VISIBLE);

            binding.tdeeCard1.setVisibility(View.VISIBLE);
            binding.manualTdeeCard.setVisibility(View.VISIBLE);
            binding.getAutomaticGoalBtn.setVisibility(View.GONE);

            isUpdatingManually = false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        CustomUIMethods.setBottomNavBar(this, null, binding.bottomNavigationView, item);

        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b)
    {
        int id = view.getId();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
    {

    }

    @Override
    public void afterTextChanged(Editable editable)
    {
        onTDEEInformationChanged.invoke();
    }

    private void setActivityLevel(TextView buttonClicked, WorkoutIntensity intensityLevel)
    {
        CustomUIMethods.setNListButton(this, activityButtonToSelectViewMap, buttonClicked, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
        currentIntensity = intensityLevel;

        onTDEEInformationChanged.invoke();
    }

    private void setMacroSplit(TextView buttonClicked, int prPercent, int caPercent, int faPercent)
    {
        CustomUIMethods.setNListButton(this, splitButtonToSelectViewMap, buttonClicked, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);

        currentSplit[0] = prPercent;
        currentSplit[1] = caPercent;
        currentSplit[2] = faPercent;

        onTDEEInformationChanged.invoke();
    }

    private void clearMeasurementFields()
    {
        binding.weightLabelText.setText("Weight");
        binding.weightLabelText.setVisibility(View.GONE);

        CustomUIMethods.setPopupMessage(this, binding.weightErrorLabelText , R.color.darkTheme_Transparent, "");
        binding.poundsCardView.setVisibility(View.GONE);
        binding.kgCardView.setVisibility(View.GONE);

        binding.heightLabelText.setText("Height");
        binding.heightLabelText.setVisibility(View.GONE);

        CustomUIMethods.setPopupMessage(this, binding.heightErrorLabelText, R.color.darkTheme_Transparent, "");
        binding.heightLinearLayout.setVisibility(View.GONE);
        binding.cmCardView.setVisibility(View.GONE);
    }

    private void setMetricFields()
    {
        clearMeasurementFields();

        binding.weightLabelText.setText("Weight (lbs)");
        binding.weightLabelText.setVisibility(View.VISIBLE);

        binding.poundsCardView.setVisibility(View.VISIBLE);

        binding.heightLabelText.setText("Height (ft/in)");
        binding.heightLabelText.setVisibility(View.VISIBLE);

        binding.weightGoalTextField.setHint("lbs");

        binding.heightLinearLayout.setVisibility(View.VISIBLE);

        onTDEEInformationChanged.invoke();
    }

    private void setImperialFields()
    {
        clearMeasurementFields();

        binding.weightLabelText.setText("Weight (kg)");
        binding.weightLabelText.setVisibility(View.VISIBLE);

        binding.kgCardView.setVisibility(View.VISIBLE);

        binding.heightLabelText.setText("Height (cm)");
        binding.heightLabelText.setVisibility(View.VISIBLE);

        binding.weightGoalTextField.setHint("kgs");

        binding.cmCardView.setVisibility(View.VISIBLE);

        onTDEEInformationChanged.invoke();
    }

    private void setActiveCard(int card)
    {
        binding.tdeeCard1.setVisibility(View.GONE);
        binding.tdeeCard2.setVisibility(View.GONE);
        binding.tdeeCard3.setVisibility(View.GONE);

        switch (card)
        {
            case 1:
                binding.tdeeCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.tdeeCard2.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.tdeeCard3.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void closeCards()
    {
        binding.manualTdeeCard.setVisibility(View.GONE);
        binding.tdeeCard1.setVisibility(View.GONE);
        binding.tdeeCard2.setVisibility(View.GONE);
        binding.tdeeCard3.setVisibility(View.GONE);
    }

    public void setCalorieField()
    {
        int age;
        double weight;
        double height;
        int calsPerDay;

        try
        {
            age = Integer.parseInt(binding.ageTextField.getText().toString());

            if (isMetric)
            {
                weight = getImperialWeight(Integer.parseInt(binding.poundsTextField.getText().toString()));
                height = getImperialHeight(Integer.parseInt(binding.feetTextField.getText().toString()), Integer.parseInt(binding.inchTextField.getText().toString()));
                calsPerDay = (int) Math.ceil(getAverageCaloriesPerDayFromLbsPerWeek(Double.parseDouble(binding.weightGoalTextField.getText().toString())));
            }
            else
            {
                weight = Integer.parseInt(binding.kgTextField.getText().toString());
                height = Integer.parseInt(binding.cmHeightField.getText().toString());
                calsPerDay = (int) Math.ceil(getAverageCaloriesPerDayFromKgsPerWeek(Double.parseDouble(binding.weightGoalTextField.getText().toString())));
            }
        }
        catch (NumberFormatException e)
        {
            return;
        }

        currentCalorieResult = (int) Math.ceil(fitManager.getTDEE(weight, height, age, isMale, currentIntensity, calsPerDay));
        binding.calorieResultLabelText.setText(currentCalorieResult + " Calories");
    }

    private void setGoal()
    {
        if (currentCalorieResult > 0 && !isUpdatingManually)
        {
            currentSplit = CustomConversionMethods.getMacrosFromSplit(currentCalorieResult, currentSplit);

            dbManager.updateGoals(CurrentProfile.id, currentCalorieResult, currentSplit[0], currentSplit[1], currentSplit[2], new EventCallback()
            {
                @Override
                public void onSuccess(@Nullable EventContext context)
                {
                    dbManager.getUser(CurrentProfile.email, new EventCallback()
                    {
                        @Override
                        public void onSuccess(@Nullable EventContext context)
                        {
                            onTDEEUserGoalUpdated.invoke();
                            finish();
                        }

                        @Override
                        public void onFailure(@Nullable EventContext context)
                        {

                        }
                    });
                }

                @Override
                public void onFailure(@Nullable EventContext context)
                {
                    //Error updating automatically
                }
            });
        }
        else if (isUpdatingManually)
        {
            int manualCals = 0;

            try
            {
                manualCals = Integer.parseInt(binding.calorieTextField.getText().toString());
                currentSplit[0] = Integer.parseInt(binding.proteinTextField.getText().toString());
                currentSplit[1] = Integer.parseInt(binding.carbsTextField.getText().toString());
                currentSplit[2] = Integer.parseInt(binding.fatTextField.getText().toString());

                if (Arrays.stream(currentSplit).sum() != 100)
                {
                    return;
                }

                currentSplit = getMacrosFromSplit(manualCals, currentSplit);

                if (manualCals > 0)
                {
                    dbManager.updateGoals(CurrentProfile.id, manualCals, currentSplit[0], currentSplit[1], currentSplit[2], new EventCallback()
                    {
                        @Override
                        public void onSuccess(@Nullable EventContext context)
                        {
                            dbManager.getUser(CurrentProfile.email, new EventCallback()
                            {
                                @Override
                                public void onSuccess(@Nullable EventContext context)
                                {
                                    onTDEEUserGoalUpdated.invoke();
                                    finish();
                                }

                                @Override
                                public void onFailure(@Nullable EventContext context)
                                {

                                }
                            });
                        }

                        @Override
                        public void onFailure(@Nullable EventContext context)
                        {
                            //Error updating manually
                        }
                    });
                }
            }
            catch (NumberFormatException e)
            {
                //Error
            }
        }
    }
}