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
import com.google.gson.Gson;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<FoodProfile> profileList;


    public FoodListAdapter(Context context, ArrayList<FoodProfile> profileList, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public FoodListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_list_view, parent, false);
        return new FoodListAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListAdapter.MyViewHolder holder, int position) {
        FoodProfile curProfile = profileList.get(position);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.generateRandomColorBasedOffBrand(), holder.imageIconText, curProfile.name);
        holder.itemName.setText(curProfile.name);
        Gson gson = new Gson();
        // is common / brand | xxx cal | xxx protein
        String detailString = curProfile.isCommon ? "Common" : curProfile.brandName + " | "
                + (int) curProfile.nutrition.calories + " Cal | "
                + curProfile.nutrition.nutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.none)).first.intValue() + " P";
        holder.itemDetails.setText(detailString);

        if (curProfile.isVerified) {
            holder.itemVerified.setVisibility(View.VISIBLE);
        } else {
            holder.itemVerified.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView imageIcon;
        TextView imageIconText;
        TextView itemName;
        TextView itemDetails;
        ImageView itemVerified;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageIcon = itemView.findViewById(R.id.listItemIcon);
            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemName = itemView.findViewById(R.id.listItemName);
            itemDetails = itemView.findViewById(R.id.listItemDetails);
            itemVerified = itemView.findViewById(R.id.listItemVerified);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
