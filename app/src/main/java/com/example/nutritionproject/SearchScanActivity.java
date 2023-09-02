package com.example.nutritionproject;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutritionproject.Custom.java.CustomUIMethods;

// Ask permission for camera
// Start scanning
// find barcode

public class SearchScanActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int CAMERA_PERMISSION_CODE = 101;
    private final CustomUIMethods uiManager = new CustomUIMethods();
    private ImageView backBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scan);

        uiManager.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);

        backBtn.setOnClickListener(this);

        checkPermissionsAndStartScanner();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.backButton) {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void checkPermissionsAndStartScanner() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startScanner();
        }
    }

    private void startScanner() {

    }
}