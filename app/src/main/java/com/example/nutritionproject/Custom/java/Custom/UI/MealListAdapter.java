package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
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
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder> {
    Context context;
    ArrayList<MealProfile> profileList;


    public MealListAdapter(Context context, ArrayList<MealProfile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public MealListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_list_view, parent, false);
        return new MealListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.MyViewHolder holder, int position) {
        MealProfile curProfile = profileList.get(position);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.getRandomLightColorHex(), holder.imageIconText, curProfile.mealProfile.name);
        holder.itemName.setText(curProfile.mealProfile.name);
        Gson gson = new Gson();
        // is common / brand | xxx cal | xxx protein
        String detailString = curProfile.mealProfile.nutrition.calories + " Cal | "
                + (curProfile.mealProfile.nutrition.nutrients.getOrDefault(Nutrient.Protein, 0.0)).intValue()  + " Pro | "
                + curProfile.mealProfile.dateAdded + " Time";
        holder.itemDetails.setText(detailString);

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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageIcon = itemView.findViewById(R.id.listItemIcon);
            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemName = itemView.findViewById(R.id.listItemName);
            itemDetails = itemView.findViewById(R.id.listItemDetails);
        }
    }
}
