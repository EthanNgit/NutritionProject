package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
import android.graphics.Paint;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<FoodProfile> profileList;
    Context context;


    public FoodListAdapter(Context context, ArrayList<FoodProfile> profileList, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.profileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_list_view, parent, false);
        FoodListAdapter.MyViewHolder foodViewHolder = new FoodListAdapter.MyViewHolder(view, recyclerViewInterface);

        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.MyViewHolder holder, int position)
    {
        FoodProfile curProfile = profileList.get(position);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.generateRandomColorBasedOffBrand(), holder.imageIconText, curProfile.name);

        String detailString = curProfile.isCommon ? "Common" : curProfile.brandName + " | "
                + (int) curProfile.nutrition.calories + " Cal | "
                + curProfile.nutrition.nutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.none)).first.intValue() + " P";

        holder.itemName.setText(curProfile.name);
        holder.itemDetails.setText(detailString);
        holder.itemVerified.setVisibility((curProfile.isVerified) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount()
    {
        int profileListSize = profileList.size();

        return profileListSize;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView imageIconText;
        ImageView itemVerified;
        TextView itemDetails;
        CardView imageIcon;
        TextView itemName;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);

            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemVerified = itemView.findViewById(R.id.listItemVerified);
            itemDetails = itemView.findViewById(R.id.listItemDetails);
            imageIcon = itemView.findViewById(R.id.listItemIcon);
            itemName = itemView.findViewById(R.id.listItemName);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
