package com.example.nutritionproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageAnalysis;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;

import org.checkerframework.checker.i18nformatter.qual.I18nMakeFormat;

public class AddFoodItemActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        CustomUIMethods.setAndroidUI(this, R.color.darkTheme_Background);

        backBtn = findViewById(R.id.backButton);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == backBtn.getId()) {
            //Give Warning about save if the fields changed
            finish();
        }
    }
}