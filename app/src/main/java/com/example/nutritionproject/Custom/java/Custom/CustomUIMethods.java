package com.example.nutritionproject.Custom.java.Custom;

import static com.example.nutritionproject.Custom.java.Custom.CustomDBMethods.CurrentProfile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.nutritionproject.DashboardHomeActivity;
import com.example.nutritionproject.DashboardSearchActivity;
import com.example.nutritionproject.DashboardStatsActivity;
import com.example.nutritionproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

public class CustomUIMethods
{
    private static boolean keyboardShown;

    /**
     * @param context Context in which this function will use
     * @param editTexts Array of all textFields that will be changed
     * @param background int id of the drawable background to be set to
     */
    public static void setTextFieldBackgrounds(Context context, EditText[] editTexts, int background)
    {
        Drawable backgroundDrawable = ContextCompat.getDrawable(context, background);

        for (EditText textField : editTexts)
        {
            textField.setBackground(backgroundDrawable);
        }
    }

    /**
     * @param view TextView that holds the error
     * @param backgroundColor Background color of the textview
     * @param errorMessage errorMessage as a string, pass "" to remove the error message
     */
    public static void setPopupMessage(Context context,TextView view, int backgroundColor, String errorMessage)
    {
        view.setBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        view.setVisibility(errorMessage.isEmpty() ? view.GONE : view.VISIBLE);
        view.setText(errorMessage);
    }

    /**
     * @apiNote Use this onCreate to set the navbar to the background color and status bar to transparent mode (has to be done per activity)
     */
    public static void setAndroidUI(Activity activity, int backgroundColor)
    {
        setNavbarToBackground(activity, backgroundColor);
        setStatusBarToTransparent(activity);
    }

    /**
     * @apiNote Use this onCreate to set the status bar to transparent mode (has to be done per activity)
     */
    public static void setStatusBarToTransparent(Activity activity)
    {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * @apiNote Use this onCreate to set the navbar to the background color (has to be done per activity)
     */
    public static void setNavbarToBackground(Activity activity, int color)
    {
        activity.getWindow().setNavigationBarDividerColor(ContextCompat.getColor(activity, color));
        activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, color));
    }

    /**
     * @param items set all items in a menu to unchecked
     */
    public static void uncheckAllNavItems(Menu items)
    {
        for (int i = 0; i < items.size(); i++)
        {
            items.getItem(i).setChecked(false);
        }
    }

    /**
     * @param item Menu Item that will be set to checked
     */
    public static void checkNavItem(MenuItem item)
    {
        item.setChecked(true);
    }

    /**
     * @param context Context in which this function will
     * @param optionOne First button option
     * @param optionTwo Second button option
     * @param selectorImageOne Underlay visual image (placed under first selected button)
     * @param selectorImageTwo Underlay visual image (placed under second selected button)
     * @param firstOption Is this the first option?
     * @param selectedTextColor Text color of the selected button
     * @param deselectedTextColor Text color of the unselected button
     */
    public static void setTwoWayButton(Context context, TextView optionOne, TextView optionTwo, ImageView selectorImageOne, ImageView selectorImageTwo, boolean firstOption, int selectedTextColor, int deselectedTextColor)
    {
        if (firstOption)
        {
            optionTwo.setTextColor(ContextCompat.getColor(context, deselectedTextColor));
            optionOne.setTextColor(ContextCompat.getColor(context, selectedTextColor));
            selectorImageOne.setVisibility(View.VISIBLE);
            selectorImageTwo.setVisibility(View.GONE);
        }
        else
        {
            optionOne.setTextColor(ContextCompat.getColor(context, deselectedTextColor));
            optionTwo.setTextColor(ContextCompat.getColor(context, selectedTextColor));
            selectorImageTwo.setVisibility(View.VISIBLE);
            selectorImageOne.setVisibility(View.GONE);
        }
    }

    /**
     * @param context Context in which this function will use
     * @param buttonToVisualMap Map in format of <Button, Visual>
     * @param currentView Button that has been Selected
     * @param selectedTextColor Text color of the selected button
     * @param unselectedTextColor Text color of the unselected button
     */
    public static void setNListButton(Context context, Map<TextView, ImageView> buttonToVisualMap, TextView currentView, int selectedTextColor, int unselectedTextColor)
    {
        if (!buttonToVisualMap.containsKey(currentView)) return;

        int selectedColor = ContextCompat.getColor(context, selectedTextColor);
        int unselectedColor = ContextCompat.getColor(context, unselectedTextColor);

        for (Map.Entry<TextView, ImageView> entry: buttonToVisualMap.entrySet())
        {
            entry.getValue().setVisibility(View.INVISIBLE);
            entry.getKey().setTextColor(unselectedColor);
        }

        buttonToVisualMap.get(currentView).setVisibility(View.VISIBLE);
        currentView.setTextColor(selectedColor);
    }

    /**
     *
     * @param context Context in which this function will use
     * @param dropdownIcon The indicator icon, usually an arrow
     * @param drowdownMenu The menu / contents this dropdown will display
     */
    public static void toggleDropdown(Context context, View dropdownIcon, View drowdownMenu)
    {
        int selectedColor = ContextCompat.getColor(context, R.color.darkTheme_WhiteFull);
        int unselectedColor = ContextCompat.getColor(context, R.color.darkTheme_WhiteFull);

        if (drowdownMenu.getVisibility() != View.VISIBLE)
        {
            drowdownMenu.setVisibility(View.VISIBLE);
            dropdownIcon.setScaleY(-1);
        }
        else
        {
            drowdownMenu.setVisibility(View.GONE);
            dropdownIcon.setScaleY(1);
        }
    }

    /**
     * @param context Context in which this function will use
     * @param editText the editText you want to force keyboard on
     */
    public static void showKeyboard(Context context, EditText editText)
    {
        editText.requestFocus();
        keyboardShown = true;

        editText.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }, 100);
    }

    /**
     * @param context Context in which this function will use
     */
    public static void hideKeyboard(Context context)
    {
        if (!keyboardShown) return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * @apiNote Returns an array of random values for R, G, B that result in a light color
     */
    public static float[] getRandomLightColorHex() {
        Random rand = new Random();
        float[] rgbArray = new float[3];

        float r = (float) (rand.nextFloat() / 2f + 0.5);
        float g = (float) (rand.nextFloat() / 2f + 0.5);
        float b = (float) (rand.nextFloat() / 2f + 0.5);

        rgbArray[0] = r;
        rgbArray[1] = g;
        rgbArray[2] = b;

        return rgbArray;
    }

    /**
     * @param context Context in which this function will use
     * @param profileButton Button that is also the background Image
     * @param profileButtonText Text that displays the Initial of the users email
     */
    public static void setProfileButton(Context context, CardView profileButton, float[] BackgroundColorHexList, TextView profileButtonText, String name)
    {
        if (CurrentProfile == null) return;

        float[] rgbArray = CurrentProfile.userColorHex;
        int colorFromRGB = Color.rgb(rgbArray[0], rgbArray[1], rgbArray[2]);
        ColorStateList colorStateList = ColorStateList.valueOf(colorFromRGB);

        profileButtonText.setText(String.valueOf(name.charAt(0)).toUpperCase());
        profileButton.setBackgroundTintList(colorStateList);
    }

    /**
     * @param context Context in which this function will use
     * @param bottomNavView the layouts bottomNavigationView
     * @param item the item that has been selected from the bottomNavigationView
     */
    public static void setBottomNavBar(Context context, @Nullable ActivityResultLauncher<Intent> activityResultLauncher, BottomNavigationView bottomNavView, MenuItem item)
    {
        int id = item.getItemId();

        if (!shouldFinishActivity(context, id)) return;

        uncheckAllNavItems(bottomNavView.getMenu());

        if (id == R.id.homeBtn)
        {
            Intent intent = new Intent(((Activity)context), DashboardHomeActivity.class);
            launchActivity(context, activityResultLauncher, intent);

            ((Activity) context).finish();

            checkNavItem(item);
        }
        else if (id == R.id.searchBtn)
        {
            Intent intent = new Intent(((Activity)context), DashboardSearchActivity.class);
            launchActivity(context, activityResultLauncher, intent);

            ((Activity) context).finish();

            checkNavItem(item);
        }
        else if (id == R.id.statsBtn)
        {
            Intent intent = new Intent(((Activity)context), DashboardStatsActivity.class);
            launchActivity(context, activityResultLauncher, intent);

            ((Activity) context).finish();

            checkNavItem(item);
        }
    }

    private static void launchActivity(Context context, @Nullable ActivityResultLauncher<Intent> activityResultLauncher, Intent intent)
    {
        if (activityResultLauncher == null)
        {
            context.startActivity(intent);
        }
        else
        {
            activityResultLauncher.launch(intent);
        }
    }

    private static boolean shouldFinishActivity(Context context, int id)
    {
        if (context.getClass() == DashboardHomeActivity.class && id == R.id.homeBtn)
        {
            return false;
        }
        else if (context.getClass() == DashboardSearchActivity.class && id == R.id.searchBtn)
        {
            return false;
        }
        else if (context.getClass() == DashboardStatsActivity.class && id == R.id.statsBtn)
        {
            return false;
        }

        return true;
    }

    /**
     * @param context Context in which this function will use
     * @param macroAmount The current value that the macro is at
     * @param selectedColor The color of the highlighted values
     * @param unselectedColor The color of the non-highlighted values
     * @return
     */
    public static SpannableStringBuilder getMultiColouredMacroText(Context context, int macroAmount, @Nullable String macroTag, int selectedColor, int unselectedColor)
    {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

        String macroAmtString = String.valueOf(macroAmount);
        SpannableString macroAmtSpannable = new SpannableString(macroAmtString);
        macroAmtSpannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(selectedColor)), 0, macroAmtString.length(), 0);

        stringBuilder.append(macroAmtSpannable);

        if (macroTag != null)
        {
            SpannableString macroTagSpannable = new SpannableString(macroTag);
            macroTagSpannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(unselectedColor)), 0, macroTag.length(), 0);

            stringBuilder.append(macroTagSpannable);
        }

        return stringBuilder;
    }


    public static float[] generateRandomColorBasedOffBrand()
    {
        Random random = new Random();

        // Define the range for each color channel (R, G, B) as floats between 0 and 1
        float min = 0.0f;
        float max = 1.0f;

        // Initialize an array to store the RGB values as floats
        float[] color = new float[3];

        // Generate random RGB values within the specified range
        color[0] = random.nextFloat() * (max - min) + min; // Red
        color[1] = random.nextFloat() * (max - min) + min; // Green
        color[2] = random.nextFloat() * (max - min) + min; // Blue

        // Ensure the generated color has sufficient contrast with both charcoal and orange
        while (!hasGoodContrast(color))
        {
            color[0] = random.nextFloat() * (max - min) + min;
            color[1] = random.nextFloat() * (max - min) + min;
            color[2] = random.nextFloat() * (max - min) + min;
        }

        return color;
    }

    private static boolean hasGoodContrast(float[] color)
    {
        // Calculate the contrast between the generated color and charcoal and orange
        float[] charcoal = {0.0f, 0.0f, 0.0f};
        float[] orange = {1.0f, 0.65f, 0.15f};

        // You can adjust the minimum contrast value as needed
        float minContrast = 0.2f;

        // Ensure the contrast with both colors is above the minimum threshold
        return calculateContrast(color, charcoal) >= minContrast && calculateContrast(color, orange) >= minContrast;
    }

    private static float calculateContrast(float[] color1, float[] color2)
    {
        // Calculate contrast using the formula for relative luminance (Y)
        float luminance1 = 0.299f * color1[0] + 0.587f * color1[1] + 0.114f * color1[2];
        float luminance2 = 0.299f * color2[0] + 0.587f * color2[1] + 0.114f * color2[2];

        return Math.abs(luminance1 - luminance2);
    }

    


}
