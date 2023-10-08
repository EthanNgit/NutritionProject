package com.example.nutritionproject;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutritionLabelParser;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.UnitCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NutritionScanActivity extends AppCompatActivity implements View.OnClickListener {

    // UI
    private ImageView backBtn;
    private PreviewView previewView;
    private ImageView resetBtn;
    private ImageView doneBtn;
    private LinearLayout servingSizeLyt;
    private TextView servingSizeValue;
    private LinearLayout calorieLyt;
    private TextView calorieValue;
    private LinearLayout fatLyt;
    private TextView totalFatValue;
    private TextView saturatedFatValue;
    private TextView transFatValue;
    private LinearLayout cholesterolLyt;
    private TextView cholesterolValue;
    private LinearLayout sodiumLyt;
    private TextView sodiumValue;
    private LinearLayout potassiumLyt;
    private TextView potassiumValue;
    private LinearLayout carbLyt;
    private TextView totalCarbValue;
    private TextView dietaryFiberValue;
    private TextView totalSugarValue;
    private TextView addedSugarValue;
    private LinearLayout proteinLyt;
    private TextView proteinValue;

    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();


    // Other
    private CameraSelector cameraSelector;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider processCameraProvider;
    private Preview cameraPreview;
    private ImageAnalysis imageAnalysis;
    private NutritionLabelParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_scan);

        doUI();

        parser = new NutritionLabelParser(this, false);
        parser.onNutritionalInformationUpdated.addListener(this, "updateNutritionalUI");

        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                processCameraProvider = cameraProviderFuture.get();
                bindCameraPreview();
                bindInputAnalyzer();
            } catch (InterruptedException | ExecutionException e) {
                // Should never be reached
            }

        }, ContextCompat.getMainExecutor(this));
    }

    private void doUI() {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.back_btn);
        previewView = findViewById(R.id.preview_view);
        resetBtn = findViewById(R.id.reset_btn);
        doneBtn = findViewById(R.id.done_btn);

        servingSizeLyt = findViewById(R.id.serving_size_layout);
        servingSizeValue = findViewById(R.id.serving_size_value);
        calorieLyt = findViewById(R.id.calorie_layout);
        calorieValue = findViewById(R.id.calorie_value);
        fatLyt = findViewById(R.id.fat_layout);
        totalFatValue = findViewById(R.id.total_fat_value);
        saturatedFatValue = findViewById(R.id.saturated_fat_value);
        transFatValue = findViewById(R.id.trans_fat_value);
        cholesterolLyt = findViewById(R.id.cholesterol_layout);
        cholesterolValue = findViewById(R.id.cholesterol_value);
        sodiumLyt = findViewById(R.id.sodium_layout);
        sodiumValue = findViewById(R.id.sodium_value);
        potassiumLyt = findViewById(R.id.potassium_layout);
        potassiumValue = findViewById(R.id.potassium_value);
        carbLyt = findViewById(R.id.carbs_layout);
        totalCarbValue = findViewById(R.id.total_carb_value);
        saturatedFatValue = findViewById(R.id.saturated_fat_value);
        dietaryFiberValue = findViewById(R.id.dietary_fiber_value);
        totalSugarValue = findViewById(R.id.total_sugar_value);
        addedSugarValue = findViewById(R.id.added_sugar_value);
        proteinLyt = findViewById(R.id.protein_layout);
        proteinValue = findViewById(R.id.protein_value);

        nutrientToTextMap.put(Nutrient.Calorie, calorieValue);
        nutrientToTextMap.put(Nutrient.TotalFat, totalFatValue);
        nutrientToTextMap.put(Nutrient.SaturatedFat, saturatedFatValue);
        nutrientToTextMap.put(Nutrient.TransFat, transFatValue);
        nutrientToTextMap.put(Nutrient.Cholesterol, cholesterolValue);
        nutrientToTextMap.put(Nutrient.Sodium, sodiumValue);
        nutrientToTextMap.put(Nutrient.Potassium, potassiumValue);
        nutrientToTextMap.put(Nutrient.TotalCarb, totalCarbValue);
        nutrientToTextMap.put(Nutrient.DietaryFiber, dietaryFiberValue);
        nutrientToTextMap.put(Nutrient.TotalSugar, totalSugarValue);
        nutrientToTextMap.put(Nutrient.AddedSugar, addedSugarValue);
        nutrientToTextMap.put(Nutrient.Protein, proteinValue);

        backBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);

        servingSizeLyt.setVisibility(View.GONE);
        potassiumLyt.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();
        } else if (id == resetBtn.getId()) {
            parser.resetParse();
        } else if (id == doneBtn.getId()) {
            parser.finishParse(new UnitCallback() {
                @Override
                public void onSuccess() {
                    // If needed with success
                    finish();
                }
            });
            // If needed with failure
            finish();
        }
    }

    private void bindInputAnalyzer() {
        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation((int) previewView.getRotation())
                .build();

        Executor cameraExecutor = Executors.newSingleThreadExecutor();

        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @OptIn(markerClass = ExperimentalGetImage.class) @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                processImageProxy(recognizer, imageProxy);

                imageProxy.close();
            }
        });

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }

    @ExperimentalGetImage private void processImageProxy(TextRecognizer recognizer, ImageProxy imageProxy) {
        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // ...

                                if (visionText.getText().isEmpty()) return;

                                for (Text.TextBlock block : visionText.getTextBlocks()) {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();

                                    for (Text.Line line: block.getLines()) {
                                        parser.enqueueParse(line.getText());
                                        parser.parse();
                                    }
                                }

                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });



    }

    private void bindCameraPreview() {
        cameraPreview = new Preview.Builder()
                .setTargetRotation((int) previewView.getRotation())
                .build();

        cameraPreview.setSurfaceProvider(previewView.getSurfaceProvider());

        processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview);
    }

    public static void startScanner(Context context, @Nullable UnitCallback callback) {
        context.startActivity(new Intent(context, NutritionScanActivity.class));
    }

    public void updateNutritionalUI() {
        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> information = parser.getParsedNutrients();

        for (Nutrient nutrient: nutrientToTextMap.keySet()) {
            try {
                String measurement = information.get(nutrient).second != NutrientMeasurement.none ? information.get(nutrient).second.name() : "";
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("#####.#", otherSymbols);
                nutrientToTextMap.get(nutrient).setText(df.format(information.get(nutrient).first) + "" + measurement);
            } catch (NullPointerException e) {
                nutrientToTextMap.get(nutrient).setText(R.string.filler_value);
            }
        }
    }

}