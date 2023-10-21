package com.example.nutritionproject.Custom.java.Utility;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Event
{

    //Custom made class to mimic C# event callback, since I am comfortable with them
    private Map<Context, Set<Method>> objectToListenersMap = new HashMap<>();
    public EventCallback callback;

    public void addListener(Context context, String listener)
    {
        try
        {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context))
            {
                objectToListenersMap.get(context).add(method);
            }
            else
            {
                objectToListenersMap.put(context, new HashSet<>(Arrays.asList(method)));
            }

        }
        catch (NoSuchMethodException e)
        {

        }
    }

    public void removeListener(Context context, String listener)
    {
        try
        {
            Method method = context.getClass().getDeclaredMethod(listener);

            if (objectToListenersMap.containsKey(context))
            {
                objectToListenersMap.get(context).remove(method);
            }
        }
        catch (NoSuchMethodException e)
        {

        }
    }

    public void invoke()
    {
        for (Context context : objectToListenersMap.keySet())
        {
            for (Method method : objectToListenersMap.get(context))
            {
                try
                {
                    method.invoke(context);
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {

                }
            }
        }
    }
}


