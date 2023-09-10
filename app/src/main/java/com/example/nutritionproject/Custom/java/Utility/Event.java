package com.example.nutritionproject.Custom.java.Utility;

import android.content.Context;
import android.util.Log;

import com.example.nutritionproject.Custom.java.Custom.CustomUtilityMethods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event {

    //Custom made class to mimic C# event callback, since I am comfortable with them
    public EventCallback callback;
    private Map<Context, Set<Method>> objectToListenersMap = new HashMap<>();

    public void addListener(Context context, String listener) {
        if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", "Added listener");
        try {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context)) {
                // add listener to list
                objectToListenersMap.get(context).add(method);
            } else {
                // put in new entry
                objectToListenersMap.put(context, new HashSet<>(Arrays.asList(method)));
            }

        } catch (NoSuchMethodException e) {
            if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", e.getMessage());
        }
    }

    public void removeListener(Context context, String listener) {
        if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", "Removed listener");
        try {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context)) {
                // remove listener from list
                objectToListenersMap.get(context).remove(method);
            }
        } catch (NoSuchMethodException e) {
            if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", e.getMessage());
        }

    }

    public void invoke()
    {
        if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", "Invocation occurred");

        for (Context context : objectToListenersMap.keySet()) {

            for (Method method : objectToListenersMap.get(context)) {
                try {
                    method.invoke(context);
                } catch (IllegalAccessException e) {
                    if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", e.getMessage());
                } catch (InvocationTargetException e) {
                    if (CustomUtilityMethods.shouldDebug(Event.class)) Log.d("NORTH_EVENT", e.getMessage());
                }
            }

        }

    }
}


