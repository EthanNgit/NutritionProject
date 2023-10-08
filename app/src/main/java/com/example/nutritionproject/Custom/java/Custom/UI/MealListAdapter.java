package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritionproject.Custom.java.Custom.CustomConversionMethods;
import com.example.nutritionproject.Custom.java.Custom.CustomUIMethods;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.FoodModel.FoodProfile;
import com.example.nutritionproject.Custom.java.FoodModel.MealProfile;
import com.example.nutritionproject.Custom.java.NutritionLabelScanner.NutrientMeasurement;
import com.example.nutritionproject.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<MealProfile> mealsList;

    public MealListAdapter(Context context, ArrayList<MealProfile> mealsList, RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.mealsList = mealsList;
    }

    @NonNull
    @Override
    public MealListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_list_view, parent, false);
        return new MealListAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.MyViewHolder holder, int position) {
        MealProfile curMeal = mealsList.get(position);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.generateRandomColorBasedOffBrand(), holder.imageIconText, curMeal.mealName);

        holder.itemName.setText(curMeal.mealName);

        double totalKcal = 0;
        double totalProt = 0;

        for (FoodProfile ingredient: curMeal.mealComposition) {
            try {
                totalKcal += ingredient.nutrition.nutrients.getOrDefault(Nutrient.Calorie, new Pair<>(0.0, NutrientMeasurement.g)).first;
                totalProt += ingredient.nutrition.nutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.g)).first;
            } catch (NullPointerException e) {
                continue;
            }

        }

        String timeString = "00:00";

        try {
            timeString = curMeal.timeAdded.substring(0, 5);
            timeString = timeString.replaceAll("-", ":");

        } catch (IndexOutOfBoundsException e) {

        }

        // is cal | Pro | time
        String detailString = (int) totalKcal + "kcal | " + (int) totalProt + "p | " + CustomConversionMethods.convertMilitaryTimeToStandardTime(timeString);
        holder.itemDetails.setText(detailString);

    }

    @Override
    public int getItemCount() {
        return mealsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView imageIcon;
        TextView imageIconText;
        TextView itemName;
        TextView itemDetails;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            imageIcon = itemView.findViewById(R.id.listItemIcon);
            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemName = itemView.findViewById(R.id.listItemName);
            itemDetails = itemView.findViewById(R.id.listItemDetails);

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
