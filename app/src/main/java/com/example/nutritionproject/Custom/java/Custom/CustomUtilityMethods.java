package com.example.nutritionproject.Custom.java.Custom;

import com.example.nutritionproject.Custom.java.Utility.Event;
import com.example.nutritionproject.FoodItemViewActivity;
import com.example.nutritionproject.SearchActivity;

public class CustomUtilityMethods {

    // All serious custom debugs start with "NORTH_".

    // Toggle these manually for debugging purposes, I like having them when I need them,
    // but hate having them when I don't. So i made this.
    public static final boolean enableDebugs = true; // Debugs in general.
    public static final boolean enableEventDebugs = false; // Debugs that derive from Event class.
    public static final boolean enableDatabaseDebugs = true; // Debugs that derive from CustomDBMethods class.
    public static final boolean enableUIDebugs = false; // Debugs that derive from CustomUIMethods class.

    public static final boolean enableSearchActivityDebugs = true; // Debugs that derive from SearchActivity class.

    public static final boolean enableRecyclerViewDebugs = true; // Debugs that derive from any Recycler view.
    public static final boolean enableProfileDebugs = true; // Debugs that derive from any Profile update view.

    /**
     * @param classToDebug Provide the class that the debug derives from, in which it will determine if they are to be debugged.
     */
    public static boolean shouldDebug(Class classToDebug) {
        if (classToDebug == Event.class) {
            return (enableDebugs && enableEventDebugs);
        } else if (classToDebug == CustomDBMethods.class) {
            return (enableDebugs && enableDatabaseDebugs);
        } else if (classToDebug == CustomUIMethods.class) {
            return (enableDebugs && enableUIDebugs);
        } else if (classToDebug == SearchActivity.class) {
            return (enableDebugs && enableSearchActivityDebugs);
        } else if (classToDebug == FoodItemViewActivity.class) {
            return (enableDebugs && enableRecyclerViewDebugs);
        }
        return true;
    }

}
