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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TdeeActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher, NavigationBarView.OnItemSelectedListener {

    //TODO: Add errors
    private CustomFitMethods fitManager = new CustomFitMethods();
    private CustomDBMethods dbManager = new CustomDBMethods();

    private Event onTDEEInformationChanged = new Event();

    public static Event onTDEEUserGoalUpdated = new Event();

    private ImageView backBtn;

    private BottomNavigationView bottomNavView;

    //region TDEE references
    //region Measurement two way button
    private TextView metricMeasurementTwoWayButton;
    private TextView imperialMeasurementTwoWayButton;
    private ImageView measurementTwoWayVisualOne;
    private ImageView measurementTwoWayVisualTwo;

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
    private ImageView genderTwoWayVisualOne;
    private ImageView genderTwoWayVisualTwo;

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

    //region goals
        private EditText weightGoalField;

    private Map<TextView, ImageView> splitButtonToSelectViewMap = new HashMap<>();

    private ImageView splitOneImageView;
    private TextView splitOneButton;

    private ImageView splitTwoImageView;
    private TextView splitTwoButton;

    private ImageView splitThreeImageView;
    private TextView splitThreeButton;

    private int[] currentSplit = new int[] {40, 30, 30};
    //endregion

    //region Cards
    private CardView tdeeCard1;
    private CardView tdeeCard2;
    private CardView tdeeCard3;

    private CardView tdeeCardManual;


    //endregion

    //region Carousel buttons
    private LinearLayout firstCarouselNextButton;
    private LinearLayout secondCarouselBackButton;
    private LinearLayout secondCarouselNextbutton;
    private LinearLayout thirdCarouselBackButton;

    //endregion

    //region Manual
    private EditText calorieGoalTextField;
    private EditText proteinPercentTextField;
    private EditText carbPercentTextField;
    private EditText fatPercentTextField;

    //endregion

    private TextView resultCalorieTxt;
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
        measurementTwoWayVisualOne = findViewById(R.id.measurementTwoWayVisualOne);
        measurementTwoWayVisualTwo = findViewById(R.id.measurementTwoWayVisualTwo);
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
        genderTwoWayVisualOne = findViewById(R.id.genderTwoWayVisualOne);
        genderTwoWayVisualTwo = findViewById(R.id.genderTwoWayVisualTwo);
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
            weightGoalField = findViewById(R.id.weightGoalTextField);

            splitOneButton = findViewById(R.id.splitOneButton);
            splitOneImageView = findViewById(R.id.splitOneViewer);

            splitTwoButton = findViewById(R.id.splitTwoButton);
            splitTwoImageView = findViewById(R.id.splitTwoViewer);

            splitThreeButton = findViewById(R.id.splitThreeButton);
            splitThreeImageView = findViewById(R.id.splitThreeViewer);

        //endregion

        //region Finding Card components
        tdeeCard1 = findViewById(R.id.tdeeCard1);
        tdeeCard2 = findViewById(R.id.tdeeCard2);
        tdeeCard3 = findViewById(R.id.tdeeCard3);
        tdeeCardManual = findViewById(R.id.manualTdeeCard);

        //endregion

        //region Finding CarouselComponents
        firstCarouselNextButton = findViewById(R.id.carouselForwardFirstButton);
        secondCarouselBackButton = findViewById(R.id.carouselBackSecondButton);
        secondCarouselNextbutton = findViewById(R.id.carouselForwardSecondButton);
        thirdCarouselBackButton = findViewById(R.id.carouselBackThirdButton);

        //endregion

        //region Finding Manual components
        calorieGoalTextField = findViewById(R.id.calorieTextField);
        proteinPercentTextField = findViewById(R.id.proteinTextField);
        carbPercentTextField = findViewById(R.id.carbsTextField);
        fatPercentTextField = findViewById(R.id.fatTextField);
        //endregion

        resultCalorieTxt = findViewById(R.id.calorieResultLabelText);
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
        //region Filling splitButtonToSelectViewMap
        splitButtonToSelectViewMap.put(splitOneButton, splitOneImageView);
        splitButtonToSelectViewMap.put(splitTwoButton, splitTwoImageView);
        splitButtonToSelectViewMap.put(splitThreeButton, splitThreeImageView);
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

        for (TextView button : splitButtonToSelectViewMap.keySet()) {
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

        weightGoalField.addTextChangedListener(this);

        calorieGoalTextField.addTextChangedListener(this);
        proteinPercentTextField.addTextChangedListener(this);
        carbPercentTextField.addTextChangedListener(this);
        fatPercentTextField.addTextChangedListener(this);


        //TODO: ADD NEW FIELD TEXT CHANGED LISTENERS

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
            CustomUIMethods.setTwoWayButton(this, metricMeasurementTwoWayButton, imperialMeasurementTwoWayButton, measurementTwoWayVisualOne, measurementTwoWayVisualTwo, true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMetric = true;
            setMetricFields();
        } else if (id == imperialMeasurementTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, metricMeasurementTwoWayButton, imperialMeasurementTwoWayButton, measurementTwoWayVisualOne, measurementTwoWayVisualTwo, false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMetric = false;
            setImperialFields();
        }
        //endregion

        //region Gender Two way button OnClick
        if (id == maleGenderTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, maleGenderTwoWayButton, femaleGenderTwoWayButton, genderTwoWayVisualOne, genderTwoWayVisualTwo, true, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
            isMale = true;
            onTDEEInformationChanged.invoke();
        } else if (id == femaleGenderTwoWayButton.getId()) {
            CustomUIMethods.setTwoWayButton(this, maleGenderTwoWayButton, femaleGenderTwoWayButton, genderTwoWayVisualOne, genderTwoWayVisualTwo, false, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
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

        //region Split N button group OnClick
        if (id == splitOneButton.getId()) {
            setMacroSplit(splitOneButton,40, 30, 30);

        } else if (id == splitTwoButton.getId()) {
            setMacroSplit(splitTwoButton,25, 30, 45);

        } else if (id == splitThreeButton.getId()) {
            setMacroSplit(splitThreeButton,34, 33, 43);

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

            closeCards();
            tdeeCardManual.setVisibility(View.VISIBLE);

            manualSetGoalButton.setVisibility(View.GONE);
            automaticGetGoalButton.setVisibility(View.VISIBLE);

            isUpdatingManually = true;

        } else if (id == automaticGetGoalButton.getId()) {
            resultCalorieTxt.setVisibility(View.VISIBLE);

            closeCards();
            tdeeCard1.setVisibility(View.VISIBLE);

            automaticGetGoalButton.setVisibility(View.GONE);
            manualSetGoalButton.setVisibility(View.VISIBLE);

            isUpdatingManually = false;
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

    private void setMacroSplit(TextView buttonClicked, int prPercent, int caPercent, int faPercent) {
        CustomUIMethods.setNListButton(this, splitButtonToSelectViewMap, buttonClicked, R.color.darkTheme_Background, R.color.darkTheme_WhiteMed);
        currentSplit[0] = prPercent;
        currentSplit[1] = caPercent;
        currentSplit[2] = faPercent;
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

        weightGoalField.setHint("lbs");

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

        weightGoalField.setHint("kgs");

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

    private void closeCards() {
        tdeeCard1.setVisibility(View.GONE);
        tdeeCard2.setVisibility(View.GONE);
        tdeeCard3.setVisibility(View.GONE);
        tdeeCardManual.setVisibility(View.GONE);
    }

    public void setCalorieField() {
        int age;
        double weight;
        double height;
        int calsPerDay;

        try {
            age = Integer.parseInt(ageTextField.getText().toString());

            if (isMetric) {
                weight = getImperialWeight(Integer.parseInt(weightMetricEditText.getText().toString()));
                height = getImperialHeight(Integer.parseInt(heightMetricFeetEditText.getText().toString()), Integer.parseInt(heightMetricInchesEditText.getText().toString()));
                calsPerDay = (int) Math.ceil(getAverageCaloriesPerDayFromLbsPerWeek(Double.parseDouble(weightGoalField.getText().toString())));
            } else {
                weight = Integer.parseInt(weightImperialEditText.getText().toString());
                height = Integer.parseInt(heightImperialEditText.getText().toString());
                calsPerDay = (int) Math.ceil(getAverageCaloriesPerDayFromKgsPerWeek(Double.parseDouble(weightGoalField.getText().toString())));
            }
        } catch (NumberFormatException e) {
            //Empty Field exists
            return;
        }

        //TODO: change goal system
        currentCalorieResult = (int) Math.ceil(fitManager.getTDEE(weight, height, age, isMale, currentIntensity, calsPerDay));
        resultCalorieTxt.setText(currentCalorieResult + " Calories");
        Log.d("NORTH_TDEE", "Calorie result changed to " + currentCalorieResult);
    }

    private void setGoal() {
        //TODO: Give errors if fields arent filled in on click
        //TODO: MAKE NEW SYSTEM FOR MACRO SPLITS

        if (currentCalorieResult > 0 && !isUpdatingManually) {
            currentSplit = CustomConversionMethods.getMacrosFromSplit(currentCalorieResult, currentSplit);

            dbManager.updateGoals(CurrentProfile.id, currentCalorieResult, currentSplit[0], currentSplit[1], currentSplit[2], new EventCallback() {
                @Override
                public void onSuccess(@Nullable EventContext context) {
                    dbManager.getUser(CurrentProfile.email, new EventCallback() {
                        @Override
                        public void onSuccess(@Nullable EventContext context) {
                            onTDEEUserGoalUpdated.invoke();
                            finish();
                        }

                        @Override
                        public void onFailure(@Nullable EventContext context) {

                        }
                    });

                }

                @Override
                public void onFailure(@Nullable EventContext context) {
                    //Error updating automatically
                }
            });
        } else if (isUpdatingManually) {
            int manualCals = 0;

            try {
                manualCals = Integer.parseInt(calorieGoalTextField.getText().toString());
                currentSplit[0] = Integer.parseInt(proteinPercentTextField.getText().toString());
                currentSplit[1] = Integer.parseInt(carbPercentTextField.getText().toString());
                currentSplit[2] = Integer.parseInt(fatPercentTextField.getText().toString());

                if (Arrays.stream(currentSplit).sum() != 100) {
                    //Sum error
                    return;
                }

                currentSplit = getMacrosFromSplit(manualCals, currentSplit);

                if (manualCals > 0) {
                    dbManager.updateGoals(CurrentProfile.id, manualCals, currentSplit[0], currentSplit[1], currentSplit[2], new EventCallback() {
                        @Override
                        public void onSuccess(@Nullable EventContext context) {
                            dbManager.getUser(CurrentProfile.email, new EventCallback() {
                                @Override
                                public void onSuccess(@Nullable EventContext context) {
                                    onTDEEUserGoalUpdated.invoke();
                                    finish();
                                }

                                @Override
                                public void onFailure(@Nullable EventContext context) {

                                }
                            });
                        }

                        @Override
                        public void onFailure(@Nullable EventContext context) {
                            //Error updating manually
                        }
                    });
                }

            } catch (NumberFormatException e) {
                //Error
            }


        }


    }

    //endregion
}