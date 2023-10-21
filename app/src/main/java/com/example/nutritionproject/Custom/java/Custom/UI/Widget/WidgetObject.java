package com.example.nutritionproject.Custom.java.Custom.UI.Widget;

public class WidgetObject
{
    public int widgetId;
    public String widgetName;
    public int widgetIconId;
    public int widgetIconColorId;
    public boolean isEnabled;

    public WidgetObject(int widgetId, String widgetName, int widgetIconId, int widgetIconColorId, boolean isEnabled)
    {
        this.widgetId = widgetId;
        this.widgetName = widgetName;
        this.widgetIconId = widgetIconId;
        this.widgetIconColorId = widgetIconColorId;
        this.isEnabled = isEnabled;
    }
}
