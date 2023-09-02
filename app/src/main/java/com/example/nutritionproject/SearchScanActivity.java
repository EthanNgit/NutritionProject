package com.example.nutritionproject;

import android.content.pm.PackageManager;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.common.Barcode;

import java.util.concurrent.ExecutionException;

// Ask permission for camera
// Start scanning
// find barcode

public class SearchScanActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int CAMERA_PERMISSION_CODE = 101;
    private final CustomUIMethods uiManager = new CustomUIMethods();
    private ImageView backBtn;
    private PreviewView cameraPreview;
    private RelativeLayout infoView;
    private TextView upcText;

    private BarcodeScannerOptions options;
    private CameraSelector cameraSelector;
    private ListenableFuture<ProcessCameraProvider> processCameraFuture;
    private ProcessCameraProvider processCameraProvider;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scan);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);

        cameraPreview = findViewById(R.id.cameraPreview);
        infoView = findViewById(R.id.barcodeFoundLayout);
        upcText = findViewById(R.id.upcidTextLabel);

        backBtn.setOnClickListener(this);

        options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_UPC_A, Barcode.FORMAT_UPC_E)
                .build();

        cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        processCameraFuture = ProcessCameraProvider.getInstance(this);

        processCameraFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    processCameraProvider = processCameraFuture.get();
                    bindCameraPreview();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, ContextCompat.getMainExecutor(this));

        checkPermissionsAndStartScanner();
    }

    private void bindCameraPreview() {
        Preview preview = new Preview.Builder().build();

        processCameraProvider.unbindAll();

        processCameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            finish();

        }
    }

    private void checkPermissionsAndStartScanner() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            //Start camera
        }
    }



}