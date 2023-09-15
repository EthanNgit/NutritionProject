package com.example.nutritionproject;

import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getImperialHeight;
import static com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods.getImperialWeight;
import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import androidx.annotation.NonNull;
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

import com.example.nutritionproject.Custom.java.Custom.CustomDBMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomFitMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.Custom.java.Enums.WorkoutGoals;
import com.example.nutritionproject.Custom.java.Enums.WorkoutIntensity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class TdeeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, NavigationBarView.OnItemSelectedListener {

    //TODO: Add errors
    private CustomFitMethods fitManager = new CustomFitMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();

    private Event onTDEEInformationChanged = new Event();

    private TextView headerText;
    private ImageView backBtn;

    private BottomNavigationView bottomNavView;

    //region TDEE references
    //region Measurement two way button
    private TextView metricMeasurementTwoWayButton;
    private TextView imperialMeasurementTwoWayButton;
    private ImageView measurementTwoWayVisual;

    private boolean isMetric = true;
    //endregion

    //region Age
    private EditText ageTextField;
    private TextView ageErrorTxt;
    //endregion

    //region Weight
    private TextView weightTxt;
    private CardView weightMetricCardView;
    private EditText weightMetricEditText;
    private CardView weightImperialCardView;
    private EditText weightImperialEditText;
    private TextView weightErrorTxt;
    //endregion

    //region Height
    private TextView heightTxt;
    private LinearLayout heightMetricLayout;
    private EditText heightMetricFeetEditText;
    private EditText heightMetricInchesEditText;
    private CardView heightImperialCardView;
    private EditText heightImperialEditText;
    private TextView heightErrorTxt;
    //endregion

    //region Gender two way button
    private TextView maleGenderTwoWayButton;
    private TextView femaleGenderTwoWayButton;
    private ImageView genderTwoWayVisual;

    private boolean isMale = true;
    //endregion

    //region Activity buttons
    private Map<TextView, ImageView> activityButtonToSelectViewMap = new HashMap<>();
    private WorkoutIntensity currentIntensity = WorkoutIntensity.Sedentary;

    private ImageView sedentaryImageView;
    private TextView sedentaryButton;

    private ImageView lightExerciseImageView;
    private TextView lightExerciseButton;

    private ImageView moderateExerciseImageView;
    private TextView moderateExerciseButton;

    private ImageView heavyExerciseImageView;
    private TextView heavyExerciseButton;

    private ImageView athleteExerciseImageView;
    private TextView athleteExerciseButton;
    //endregion

    //region goal buttons
    private Map<TextView, ImageView> goalButtonToSelectViewMap = new HashMap<>();
    private WorkoutGoals currentGoal = WorkoutGoals.Maintain;

    private ImageView heavyCutImageView;
    private TextView heavyCutButton;

    private ImageView lightCutImageView;
    private TextView lightCutButton;

    private ImageView maintainImageView;
    private TextView maintainButton;

    private ImageView lightBulkImageView;
    private TextView lightBulkButton;

    private ImageView heavyBulkImageView;
    private TextView heavyBulkButton;
    //endregion

    //region Cards
    private CardView tdeeCard1;
    private CardView tdeeCard2;
    private CardView tdeeCard3;


    //endregion

    //region Carousel buttons
    private LinearLayout firstCarouselNextButton;
    private LinearLayout secondCarouselBackButton;
    private LinearLayout secondCarouselNextbutton;
    private LinearLayout thirdCarouselBackButton;

    //endregion

    private TextView resultCalorieTxt;

    private EditText manualCalorieField;
    private Button setGoalButton;
    private TextView manualSetGoalButton;
    private TextView automaticGetGoalButton;

    private boolean isUpdatingManually = false;

    private int currentCalorieResult = 0;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tdee);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        headerText = findViewById(R.id.secondaryHeader);
        backBtn = findViewById(R.id.backButton);
        bottomNavView = findViewById(R.id.bottomNavigationView);

        bottomNavView.setItemIconTintList(null);

        bottomNavView.getMenu().findItem(R.id.homeBtn).setChecked(true);

        bottomNavView.setOnItemSelectedListener(this);

        backBtn.setOnClickListener(this);


        //region TDEE

        onTDEEInformationChanged.addListener(this, "setCalorieField");

        //region Finding TDEE components
        //region Finding measurement components
        measurementTwoWayVisual = findViewById(R.id.measurementTwoWayVisual);
        metricMeasurementTwoWayButton = findViewById(R.id.metricMeasurementButton);
        imperialMeasurementTwoWayButton = findViewById(R.id.imperialMeasurementButton);
        //endregion

        //region Finding age components
        ageTextField = findViewById(R.id.ageTextField);
        ageErrorTxt = findViewById(R.id.ageErrorLabelText);
        //endregion

        //region Finding weight components
        weightTxt = findViewById(R.id.weightLabelText);
        weightMetricCardView = findViewById(R.id.poundsCardView);
        weightMetricEditText = findViewById(R.id.poundsTextField);
        weightImperialCardView = findViewById(R.id.kgCardView);
        weightImperialEditText = findViewById(R.id.kgTextField);
        weightErrorTxt = findViewById(R.id.weightErrorLabelText);
        //endregion

        //region Finding height components
        heightTxt = findViewById(R.id.heightLabelText);
        heightMetricLayout = findViewById(R.id.heightLinearLayout);
        heightMetricFeetEditText = findViewById(R.id.feetTextField);
        heightMetricInchesEditText = findViewById(R.id.inchTextField);
        heightImperialCardView = findViewById(R.id.cmCardView);
        heightImperialEditText = findViewById(R.id.cmHeightField);
        heightErrorTxt = findViewById(R.id.heightErrorLabelText);
        //endregion

        //region Finding gender components
        genderTwoWayVisual = findViewById(R.id.genderTwoWayVisual);
        maleGenderTwoWayButton = findViewById(R.id.maleGenderButton);
        femaleGenderTwoWayButton = findViewById(R.id.femaleGenderButton);
        //endregion

        //region Finding activity components
        sedentaryButton = findViewById(R.id.sedentaryExerciseButton);
        sedentaryImageView = findViewById(R.id.sedentaryExerciseViewer);

        lightExerciseButton = findViewById(R.id.lightExerciseButton);
        lightExerciseImageView = findViewById(R.id.lightExerciseViewer);

        moderateExerciseButton = findViewById(R.id.moderateExerciseButton);
        moderateExerciseImageView = findViewById(R.id.moderateExerciseViewer);

        heavyExerciseButton = findViewById(R.id.heavyExerciseButton);
        heavyExerciseImageView = findViewById(R.id.heavyExerciseViewer);

        athleteExerciseButton = findViewById(R.id.athleteButton);
        athleteExerciseImageView = findViewById(R.id.athleteViewer);
        //endregion

        //region Finding goal components
        heavyCutButton = findViewById(R.id.heavyCutButton);
        heavyCutImageView = findViewById(R.id.heavyCutViewer);

        lightCutButton = findViewById(R.id.lightCutButton);
        lightCutImageView = findViewById(R.id.lightCutViewer);

        maintainButton = findViewById(R.id.maintainButton);
        maintainImageView = findViewById(R.id.maintainViewer);

        lightBulkButton = findViewById(R.id.lightBulkButton);
        lightBulkImageView = findViewById(R.id.lightBulkViewer);

        heavyBulkButton = findViewById(R.id.heavyBulkButton);
        heavyBulkImageView = findViewById(R.id.heavyBulkViewer);
        //endregion

        //region Finding Card components
        tdeeCard1 = findViewById(R.id.tdeeCard1);
        tdeeCard2 = findViewById(R.id.tdeeCard2);
        tdeeCard3 = findViewById(R.id.tdeeCard3);

        //endregion

        //region Finding CarouselComponents
        firstCarouselNextButton = findViewById(R.id.carouselForwardFirstButton);
        secondCarouselBackButton = findViewById(R.id.carouselBackSecondButton);
        secondCarouselNextbutton = findViewById(R.id.carouselForwardSecondButton);
        thirdCarouselBackButton = findViewById(R.id.carouselBackThirdButton);

        //endregion

        resultCalorieTxt = findViewById(R.id.calorieResultLabelText);
        manualCalorieField = findViewById(R.id.manualResultTextField);
        
        setGoalButton = findViewById(R.id.setGoalButton);
        manualSetGoalButton = findViewById(R.id.setManualGoalButton);
        automaticGetGoalButton = findViewById(R.id.getAutomaticGoalButton);
        //endregion

        //region Filling TDEE maps
        //region Filling activityButtonToSelectViewMap
        activityButtonToSelectViewMap.put(sedentaryButton, sedentaryImageView);
        activityButtonToSelectViewMap.put(lightExerciseButton, lightExerciseImageView);
        activityButtonToSelectViewMap.put(moderateExerciseButton, moderateExerciseImageView);
        activityButtonToSelectViewMap.put(heavyExerciseButton, heavyExerciseImageView);
        activityButtonToSelectViewMap.put(athleteExerciseButton, athleteExerciseImageView);
        //endregion

        //region Filling goalButtonToSelectViewMap
        goalButtonToSelectViewMap.put(heavyCutButton, heavyCutImageView);
        goalButtonToSelectViewMap.put(lightCutButton, lightCutImageView);
        goalButtonToSelectViewMap.put(maintainButton, maintainImageView);
        goalButtonToSelectViewMap.put(lightBulkButton, lightBulkImageView);
        goalButtonToSelectViewMap.put(heavyBulkButton, heavyBulkImageView);
        //endregion
        //endregion


        //region Setting TDEE click listeners
        metricMeasurementTwoWayButton.setOnClickListener(this);
        imperialMeasurementTwoWayButton.setOnClickListener(this);

        maleGenderTwoWayButton.setOnClickListener(this);
        femaleGenderTwoWayButton.setOnClickListener(this);

        firstCarouselNextButton.setOnClickListener(this);
        secondCarouselBackButton.setOnClickListener(this);
        secondCarouselNextbutton.setOnClickListener(this);
        thirdCarouselBackButton.setOnClickListener(this);

        setGoalButton.setOnClickListener(this);

        manualSetGoalButton.setOnClickListener(this);
        automaticGetGoalButton.setOnClickListener(this);
        
        for (TextView button : activityButtonToSelectViewMap.keySet()) {
            button.setOnClickListener(this);
        }

        for (TextView button : goalButtonToSelectViewMap.keySet()) {
            button.setOnClickListener(this);
        }

        //endregion

        //region Setting TDEE field listeners
        ageTextField.setOnFocusChangeListener(this);
        weightMetricEditText.setOnFocusChangeListener(this);
        weightImperialEditText.setOnFocusChangeListener(this);
        heightMetricFeetEditText.setOnFocusChangeListener(this);
        heightMetricInchesEditText.setOnFocusChangeListener(this);
        heightImperialEditText.setOnFocusChangeListener(this);

        ageTextField.addTextChangedListener(this);
        weightMetricEditText.addTextChangedListener(this);
        weightImperialEditText.addTextChangedListener(this);
        heightMetricFeetEditText.addTextChangedListener(this);
        heightMetricInchesEditText.addTextChangedListener(this);
        heightImperialEditText.addTextChangedListener(this);

        manualCalorieField.setOnFocusChangeListener(this);
        //endregion

        //endregion
    }

    //region Override Methods
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        }

        //region TDEE OnClick
        //region Measurement Two way button OnClick
        if (id == metricMeasurementTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, metricMeasurementTwoWayButton, imperialMeasurementTwoWayButton, measurementTwoWayVisual, 370, true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMetric = true;
            setMetricFields();
        } else if (id == imperialMeasurementTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, metricMeasurementTwoWayButton, imperialMeasurementTwoWayButton, measurementTwoWayVisual, 370, false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMetric = false;
            setImperialFields();
        }
        //endregion

        //region Gender Two way button OnClick
        if (id == maleGenderTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, maleGenderTwoWayButton, femaleGenderTwoWayButton, genderTwoWayVisual, 370, true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMale = true;
            onTDEEInformationChanged.invoke();
        } else if (id == femaleGenderTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, maleGenderTwoWayButton, femaleGenderTwoWayButton, genderTwoWayVisual, 370, false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMale = false;
            onTDEEInformationChanged.invoke();
        }
        //endregion

        //region Activity N button group OnClick
        if (id == sedentaryButton.getId()) {
            setActivityLevel(sedentaryButton, WorkoutIntensity.Sedentary);

        } else if (id == lightExerciseButton.getId()) {
            setActivityLevel(lightExerciseButton, WorkoutIntensity.Light);

        } else if (id == moderateExerciseButton.getId()) {
            setActivityLevel(moderateExerciseButton, WorkoutIntensity.Moderate);

        } else if (id == heavyExerciseButton.getId()) {
            setActivityLevel(heavyExerciseButton, WorkoutIntensity.Heavy);

        } else if (id == athleteExerciseButton.getId()) {
            setActivityLevel(athleteExerciseButton, WorkoutIntensity.Athlete);

        }
        //endregion

        //region Goal N button group OnClick
        if (id == heavyCutButton.getId()) {
            setGoalLevel(heavyCutButton, WorkoutGoals.HeavyCut);

        } else if (id == lightCutButton.getId()) {
            setGoalLevel(lightCutButton, WorkoutGoals.LightCut);

        } else if (id == maintainButton.getId()) {
            setGoalLevel(maintainButton, WorkoutGoals.Maintain);

        } else if (id == lightBulkButton.getId()) {
            setGoalLevel(lightBulkButton, WorkoutGoals.LightBulk);

        } else if (id == heavyBulkButton.getId()) {
            setGoalLevel(heavyBulkButton, WorkoutGoals.HeavyBulk);

        }
        //endregion

        //region Carousel buttons OnClick

        if (id == firstCarouselNextButton.getId()) {
            setActiveCard(2);

        } else if (id == secondCarouselBackButton.getId()) {
            setActiveCard(1);

        } else if (id == secondCarouselNextbutton.getId()) {
            setActiveCard(3);

        } else if (id == thirdCarouselBackButton.getId()) {
            setActiveCard(2);

        }

        //endregion
        
        if (id == setGoalButton.getId()) {
            setGoal();

        } else if (id == manualSetGoalButton.getId()) {
            resultCalorieTxt.setVisibility(View.GONE);
            manualCalorieField.setVisibility(View.VISIBLE);

            manualCalorieField.setText("");

            manualSetGoalButton.setVisibility(View.GONE);
            automaticGetGoalButton.setVisibility(View.VISIBLE);

            CustomUIMethods.showKeyboard(this, manualCalorieField);

            isUpdatingManually = true;

        } else if (id == automaticGetGoalButton.getId()) {
            resultCalorieTxt.setVisibility(View.VISIBLE);
            manualCalorieField.setVisibility(View.GONE);

            automaticGetGoalButton.setVisibility(View.GONE);
            manualSetGoalButton.setVisibility(View.VISIBLE);

            isUpdatingManually = false;

            //uiManager.hideKeyboard(this);
        }
        //endregion
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        CustomUIMethods.setBottomNavBar(this, id, bottomNavView, item);

        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();

        //TODO: Set UX for fields on TDEE

        //region TDEE onFocusChange

        //Clear

        //Set Normal

        //Set Error
        //endregion
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        onTDEEInformationChanged.invoke();
    }
    //endregion

    //region Reusable Methods
    private void setActivityLevel(TextView buttonClicked, WorkoutIntensity intensityLevel) {
        CustomUIMethods.setNListButton(this, activityButtonToSelectViewMap, buttonClicked, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
        currentIntensity = intensityLevel;
        onTDEEInformationChanged.invoke();
    }

    private void setGoalLevel(TextView buttonClicked, WorkoutGoals goal) {
        CustomUIMethods.setNListButton(this, goalButtonToSelectViewMap, buttonClicked, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
        currentGoal = goal;
        onTDEEInformationChanged.invoke();
    }

    private void clearMeasurementFields() {
        weightTxt.setText("Weight");
        weightTxt.setVisibility(View.GONE);

        CustomUIMethods.setPopupMessage(this, weightErrorTxt , R.color.darkTheme_Transparent, "");
        weightMetricCardView.setVisibility(View.GONE);
        weightImperialCardView.setVisibility(View.GONE);

        heightTxt.setText("Height");
        heightTxt.setVisibility(View.GONE);

        CustomUIMethods.setPopupMessage(this, heightErrorTxt, R.color.darkTheme_Transparent, "");
        heightMetricLayout.setVisibility(View.GONE);
        heightImperialCardView.setVisibility(View.GONE);
    }

    private void setMetricFields() {
        clearMeasurementFields();

        weightTxt.setText("Weight (lbs)");
        weightTxt.setVisibility(View.VISIBLE);

        weightMetricCardView.setVisibility(View.VISIBLE);

        heightTxt.setText("Height (ft/in)");
        heightTxt.setVisibility(View.VISIBLE);

        heightMetricLayout.setVisibility(View.VISIBLE);

        onTDEEInformationChanged.invoke();
    }

    private void setImperialFields() {
        clearMeasurementFields();

        weightTxt.setText("Weight (kg)");
        weightTxt.setVisibility(View.VISIBLE);

        weightImperialCardView.setVisibility(View.VISIBLE);

        heightTxt.setText("Height (cm)");
        heightTxt.setVisibility(View.VISIBLE);

        heightImperialCardView.setVisibility(View.VISIBLE);

        onTDEEInformationChanged.invoke();
    }

    private void setActiveCard(int card) {
        tdeeCard1.setVisibility(View.GONE);
        tdeeCard2.setVisibility(View.GONE);
        tdeeCard3.setVisibility(View.GONE);

        switch (card) {
            case 1:
                tdeeCard1.setVisibility(View.VISIBLE);
                break;
            case 2:
                tdeeCard2.setVisibility(View.VISIBLE);
                break;
            case 3:
                tdeeCard3.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setCalorieField() {
        int age;
        double weight;
        double height;

        try {
            age = Integer.parseInt(ageTextField.getText().toString());

            if (isMetric) {
                weight = getImperialWeight(Integer.parseInt(weightMetricEditText.getText().toString()));
                height = getImperialHeight(Integer.parseInt(heightMetricFeetEditText.getText().toString()), Integer.parseInt(heightMetricInchesEditText.getText().toString()));
            } else {
                weight = Integer.parseInt(weightImperialEditText.getText().toString());
                height = Integer.parseInt(heightImperialEditText.getText().toString());
            }
        } catch (NumberFormatException e) {
            //Empty Field exists
            Log.d("NORTH_TDEE", "EMPTY FIELD EXISTS" + e.getMessage());
            return;
        }

        Log.d("NORTH_TDEE", "SET MESSAGE");
        currentCalorieResult = (int) Math.ceil(fitManager.getTDEE(weight, height, age, isMale, currentIntensity, currentGoal));
        resultCalorieTxt.setText(Integer.toString(currentCalorieResult) + " Calories");
    }

    private void setGoal() {
        //TODO: Give errors if fields arent filled in on click

        if (currentCalorieResult > 0 && !isUpdatingManually) {
            dbManager.updateGoals(CurrentProfile.id, currentCalorieResult, 0, 0,0, null);
        } else if (isUpdatingManually) {
            int manualCals = 0;

            try {
                manualCals = Integer.parseInt(manualCalorieField.getText().toString());

            } catch (NumberFormatException e) {
                //Error
                return;
            }

            if (manualCals > 0) {
                dbManager.updateGoals(CurrentProfile.id, manualCals, 0, 0,0, null);
            }
        }
    }

    //endregion
}