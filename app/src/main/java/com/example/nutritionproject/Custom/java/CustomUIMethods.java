package com.example.nutritionproject.Custom.java;

import static com.example.nutritionproject.Custom.java.CustomDBMethods.CurrentProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.nutritionproject.DashboardHomeActivity;
import com.example.nutritionproject.DashboardSearchActivity;
import com.example.nutritionproject.DashboardStatsActivity;
import com.example.nutritionproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;
import java.util.Random;

public class CustomUIMethods {

    public boolean keyboardShown;

    /**
     *
     * @param context Context in which this function will use
     * @param editTexts Array of all textFields that will be changed
     * @param background int id of the drawable background to be set to
     */
    public void setTextFieldBackgrounds(Context context, EditText[] editTexts, int background) {
        for (EditText textField : editTexts) {
            textField.setBackground(ContextCompat.getDrawable(context, background));
        }
    }

    /**
     *
     * @param view TextView that holds the error
     * @param backgroundColor Background color of the textview
     * @param errorMessage errorMessage as a string, pass "" to remove the error message
     */
    public void setPopupMessage(Context context,TextView view, int backgroundColor, String errorMessage) {
        view.setText(errorMessage);
        view.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        view.setVisibility(errorMessage.isEmpty() ? view.GONE : view.VISIBLE);

    }

    /**
     *
     * @apiNote Use this onCreate to set the navbar to the background color and status bar to transparent mode (has to be done per activity)
     *
     */
    public void setAndroidUI(Activity activity, int backgroundColor) {
        setStatusBarToTransparent(activity);
        setNavbarToBackground(activity, backgroundColor);
    }

    /**
     *
     * @apiNote Use this onCreate to set the status bar to transparent mode (has to be done per activity)
     */
    public void setStatusBarToTransparent(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     *
     * @apiNote Use this onCreate to set the navbar to the background color (has to be done per activity)
     */
    public void setNavbarToBackground(Activity activity, int color) {
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, color));
        activity.getWindow().setNavigationBarDividerColor(ContextCompat.getColor(activity, color));
    }

    /**
     *
     * @param items set all items in a menu to unchecked
     */
    public void uncheckAllNavItems(Menu items) {
        for (int i = 0; i < items.size(); i++) {
            items.getItem(i).setChecked(false);
        }
    }

    /**
     *
     * @param item Menu Item that will be set to checked
     */
    public void checkNavItem(MenuItem item) {
        item.setChecked(true);
    }

    /**
     *
     * @param context Context in which this function will
     * @param optionOne First button option
     * @param optionTwo Second button option
     * @param selectorImage Underlay visual image (placed under current selected button)
     * @param buttonWidth Length of the the entire two way button
     * @param firstOption Is this the first option?
     * @param selectedTextColor Text color of the selected button
     * @param deselectedTextColor Text color of the unselected button
     */
    public void setTwoWayButton(Context context, TextView optionOne, TextView optionTwo, ImageView selectorImage, int buttonWidth, boolean firstOption, int selectedTextColor, int deselectedTextColor) {
        ViewGroup.MarginLayoutParams  selectorImageMargins = (ViewGroup.MarginLayoutParams) selectorImage.getLayoutParams();

        float dpRatio = context.getResources().getDisplayMetrics().density;

        if (!firstOption) {
            //Toggle to second option
            int pixelForDp = (int) ((buttonWidth / 2) * dpRatio);
            selectorImageMargins.setMarginStart(pixelForDp);

            selectorImage.setLayoutParams(selectorImageMargins);

            optionOne.setTextColor(ContextCompat.getColor(context, deselectedTextColor));
            optionTwo.setTextColor(ContextCompat.getColor(context, selectedTextColor));
        } else {
            //Toggle to first option
            selectorImageMargins.setMarginStart(0);

            selectorImage.setLayoutParams(selectorImageMargins);

            optionOne.setTextColor(ContextCompat.getColor(context, selectedTextColor));
            optionTwo.setTextColor(ContextCompat.getColor(context, deselectedTextColor));
        }
    }

    /**
     *
     * @param context Context in which this function will use
     * @param buttonToVisualMap Map in format of <Button, Visual>
     * @param currentView Button that has been Selected
     * @param selectedTextColor Text color of the selected button
     * @param deselectedTextColor Text color of the unselected button
     */
    public void setNListButton(Context context, Map<TextView, ImageView> buttonToVisualMap, TextView currentView, int selectedTextColor, int deselectedTextColor) {
        if (!buttonToVisualMap.containsKey(currentView)) {
            return;
        }

        for (Map.Entry<TextView, ImageView> entry: buttonToVisualMap.entrySet()) {
            entry.getValue().setVisibility(View.INVISIBLE);
            entry.getKey().setTextColor(ContextCompat.getColor(context, deselectedTextColor));
        }

        buttonToVisualMap.get(currentView).setVisibility(View.VISIBLE);
        currentView.setTextColor(ContextCompat.getColor(context, selectedTextColor));
    }

    /**
     *
     * @param context Context in which this function will use
     * @param editText the editText you want to force keyboard on
     */
    public void showKeyboard(Context context, EditText editText) {
        keyboardShown = true;
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }, 100);
    }

    /**
     *
     * @param context Context in which this function will use
     */
    public void hideKeyboard(Context context) {
        if (keyboardShown) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    /**
     *
     * @apiNote Returns an array of random values for R, G, B that result in a light color
     */
    public static float[] getRandomLightColorHex() {
        Random rand = new Random();

        float r = (float) (rand.nextFloat() / 2f + 0.5);
        float g = (float) (rand.nextFloat() / 2f + 0.5);
        float b = (float) (rand.nextFloat() / 2f + 0.5);

        return new float[]{r, g, b};
    }

    /**
     *
     * @param context Context in which this function will use
     * @param profileButton Button that is also the background Image
     * @param profileButtonText Text that displays the Initial of the users email
     */
    public void setProfileButton(Context context, CardView profileButton, TextView profileButtonText) {
        profileButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(CurrentProfile.userColorHex[0], CurrentProfile.userColorHex[1], CurrentProfile.userColorHex[2])));
        profileButtonText.setText(String.valueOf(CurrentProfile.email.charAt(0)).toUpperCase());
    }

    /**
     *
     * @param context Context in which this function will use
     * @param id Id that is gotten from an onclick view
     * @param bottomNavView the layouts bottomNavigationView
     * @param item the item that has been selected from the bottomNavigationView
     */
    public void setBottomNavBar(Context context, int id, BottomNavigationView bottomNavView, MenuItem item) {
        uncheckAllNavItems(bottomNavView.getMenu());

        //TODO: Make easier to do this???

        if (id == R.id.homeBtn) {
            Log.d("NORTH_DASHBOARD", "Home button pressed " + item.isChecked());

            context.startActivity(new Intent(((Activity)context), DashboardHomeActivity.class));
            ((Activity) context).finish();

            checkNavItem(item);
        } else if (id == R.id.searchBtn) {
            Log.d("NORTH_DASHBOARD", "Search button pressed "+ item.isChecked());

            context.startActivity(new Intent(((Activity)context), DashboardSearchActivity.class));
            ((Activity) context).finish();

            checkNavItem(item);
        } else if (id == R.id.statsBtn) {
            Log.d("NORTH_DASHBOARD", "Stats button pressed " + item.isChecked());

            context.startActivity(new Intent(((Activity)context), DashboardStatsActivity.class));
            ((Activity) context).finish();

            checkNavItem(item);
        }
    }

}
