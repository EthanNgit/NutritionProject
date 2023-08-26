package com.example.nutritionproject.Custom;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.example.nutritionproject.R;

import java.util.Map;

public class CustomUIMethods {

    public boolean keyboardShown;

    /**
     *
     * @param context Context in which this function will use to getDrawable
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
     * @param context Context in which this function will use to getDrawable
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
     * @param context Context in which this function will use to getDrawable
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
     * @param context Context in which this function will use to getSystemService
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
     * @param context Context in which this function will use to getSystemService
     */
    public void hideKeyboard(Context context) {
        if (keyboardShown) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

}
