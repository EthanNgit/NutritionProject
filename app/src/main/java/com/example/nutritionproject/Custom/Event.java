package com.example.nutritionproject.Custom;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event {

    //Custom Made Class to mimic C# event callback, since I am comfortable with them

    private Map<Context, Set<Method>> objectToListenersMap = new HashMap<>();

    public void addListener(Context context, String listener) {
        Log.d("NORTH_EVENT", "added listener");
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
            Log.d("NORTH_EVENT", e.getMessage());
        }
    }

    public void removeListener(Context context, String listener) {
        Log.d("NORTH_EVENT", "removed listener");
        try {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context)) {
                // remove listener from list
                objectToListenersMap.get(context).remove(method);
            }
        } catch (NoSuchMethodException e) {
            Log.d("NORTH_EVENT", e.getMessage());
        }

    }

    public void invoke()
    {
        Log.d("NORTH_EVENT", "invocation occurred");

        for (Context context : objectToListenersMap.keySet()) {

            for (Method method : objectToListenersMap.get(context)) {
                try {
                    method.invoke(context);
                } catch (IllegalAccessException e) {
                    Log.d("NORTH_EVENT", e.getMessage());
                } catch (InvocationTargetException e) {
                    Log.d("NORTH_EVENT", e.getMessage());
                }
            }

        }

    }
}


