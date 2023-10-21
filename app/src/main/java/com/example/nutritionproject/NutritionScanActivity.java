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
import com.example.nutritionproject.Custom.java.Utility.EventCallback;
import com.example.nutritionproject.Custom.java.Utility.EventContext;
import com.example.nutritionproject.databinding.ActivityFoodItemViewBinding;
import com.example.nutritionproject.databinding.ActivityNutritionScanBinding;
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

public class NutritionScanActivity extends AppCompatActivity implements View.OnClickListener
{
    HashMap<Nutrient, TextView> nutrientToTextMap = new HashMap<>();
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider processCameraProvider;
    private CameraSelector cameraSelector;
    private ImageAnalysis imageAnalysis;
    private NutritionLabelParser parser;
    private Preview cameraPreview;
    private ActivityNutritionScanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityNutritionScanBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        handleUI();

        parser = new NutritionLabelParser(this, false);
        parser.onNutritionalInformationUpdated.addListener(this, "updateNutritionalUI");

        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() ->
        {
            try
            {
                processCameraProvider = cameraProviderFuture.get();
                bindCameraPreview();
                bindInputAnalyzer();
            }
            catch (InterruptedException | ExecutionException e)
            {
                // Should never be reached
            }

        }, ContextCompat.getMainExecutor(this));
    }

    private void handleUI()
    {
        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

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

        binding.backBtn.setOnClickListener(this);
        binding.resetBtn.setOnClickListener(this);
        binding.doneBtn.setOnClickListener(this);

        binding.servingSizeLayout.setVisibility(View.GONE);
        binding.potassiumLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == binding.backBtn.getId())
        {
            finish();
        }
        else if (id == binding.resetBtn.getId())
        {
            parser.resetParse();
        }
        else if (id == binding.doneBtn.getId())
        {
            parser.finishParse(new UnitCallback()
            {
                @Override
                public void onSuccess()
                {
                    finish();
                }
            });

            finish();
        }
    }

    private void bindInputAnalyzer()
    {
        TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetRotation((int) binding.previewView.getRotation())
                .build();

        Executor cameraExecutor = Executors.newSingleThreadExecutor();

        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer()
        {
            @OptIn(markerClass = ExperimentalGetImage.class) @Override
            public void analyze(@NonNull ImageProxy imageProxy)
            {
                processImageProxy(recognizer, imageProxy);

                imageProxy.close();
            }
        });

        processCameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }

    @ExperimentalGetImage private void processImageProxy(TextRecognizer recognizer, ImageProxy imageProxy)
    {
        InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());

        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>()
                        {
                            @Override
                            public void onSuccess(Text visionText)
                            {
                                if (visionText.getText().isEmpty()) return;

                                for (Text.TextBlock block : visionText.getTextBlocks())
                                {
                                    Rect boundingBox = block.getBoundingBox();
                                    Point[] cornerPoints = block.getCornerPoints();
                                    String text = block.getText();

                                    for (Text.Line line: block.getLines())
                                    {
                                        parser.enqueueParse(line.getText());
                                        parser.parse();
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener()
                                {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }

    private void bindCameraPreview()
    {
        cameraPreview = new Preview.Builder()
                .setTargetRotation((int)  binding.previewView.getRotation())
                .build();

        cameraPreview.setSurfaceProvider( binding.previewView.getSurfaceProvider());

        processCameraProvider.bindToLifecycle(this, cameraSelector, cameraPreview);
    }

    public static void startScanner(Context context, @Nullable UnitCallback callback)
    {
        context.startActivity(new Intent(context, NutritionScanActivity.class));
    }

    public void updateNutritionalUI()
    {
        HashMap<Nutrient, Pair<Double, NutrientMeasurement>> information = parser.getParsedNutrients();

        for (Nutrient nutrient: nutrientToTextMap.keySet())
        {
            try
            {
                String measurement = information.get(nutrient).second != NutrientMeasurement.none ? information.get(nutrient).second.name() : "";
                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("#####.#", otherSymbols);
                nutrientToTextMap.get(nutrient).setText(df.format(information.get(nutrient).first) + "" + measurement);
            }
            catch (NullPointerException e)
            {
                nutrientToTextMap.get(nutrient).setText(R.string.filler_value);
            }
        }
    }
}