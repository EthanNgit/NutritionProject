package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritionproject.Custom.java.Custom.UI.Widget.WidgetObject;
import com.example.nutritionproject.R;

import java.util.ArrayList;

public class WidgetListAdapter extends RecyclerView.Adapter<WidgetListAdapter.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<WidgetObject> widgets;
    Context context;


    public WidgetListAdapter(Context context, ArrayList<WidgetObject> widgets, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.widgets = widgets;
        this.context = context;
    }

    @NonNull
    @Override
    public WidgetListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_list_view, parent, false);
        WidgetListAdapter.MyViewHolder widgetViewHolder = new WidgetListAdapter.MyViewHolder(view, recyclerViewInterface);

        return widgetViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WidgetListAdapter.MyViewHolder holder, int position)
    {
        WidgetObject currentWidget = widgets.get(position);

        holder.itemIcon.setImageResource(0);
        holder.itemIcon.setImageDrawable(ContextCompat.getDrawable(context, currentWidget.widgetIconId));
        holder.itemName.setText(currentWidget.widgetName);

        if (currentWidget.isEnabled)
        {
            holder.itemIcon.setColorFilter(ContextCompat.getColor(context, currentWidget.widgetIconColorId));
            holder.itemName.setTextColor(ContextCompat.getColor(context, R.color.darkTheme_WhiteFull));
            holder.itemSubBtn.setVisibility(View.VISIBLE);
            holder.itemAddBtn.setVisibility(View.GONE);
        }
        else
        {
            holder.itemIcon.setColorFilter(ContextCompat.getColor(context, R.color.darkTheme_WhiteMed), PorterDuff.Mode.SRC_IN);
            holder.itemName.setTextColor(ContextCompat.getColor(context, R.color.darkTheme_WhiteMed));
            holder.itemAddBtn.setVisibility(View.VISIBLE);
            holder.itemSubBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        int widgetListSize = widgets.size();

        return widgetListSize;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemName;
        ImageView itemIcon;
        ImageView itemAddBtn;
        ImageView itemSubBtn;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);

            itemName = itemView.findViewById(R.id.listItemName);
            itemIcon = itemView.findViewById(R.id.listItemIcon);
            itemAddBtn = itemView.findViewById(R.id.listItemAddBtn);
            itemSubBtn = itemView.findViewById(R.id.listItemMinusBtn);

            itemAddBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(0, pos);
                        }
                    }
                }
            });

            itemSubBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(1, pos);
                        }
                    }
                }
            });
        }
    }
}
