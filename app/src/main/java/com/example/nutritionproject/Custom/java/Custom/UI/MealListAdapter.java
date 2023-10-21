package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<MealProfile> mealsList;
    Context context;

    public MealListAdapter(Context context, ArrayList<MealProfile> mealsList, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.mealsList = mealsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MealListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meal_list_view, parent, false);
        MealListAdapter.MyViewHolder mealViewHolder = new MealListAdapter.MyViewHolder(view, recyclerViewInterface);

        return mealViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealListAdapter.MyViewHolder holder, int position)
    {
        MealProfile curMeal = mealsList.get(position);
        String timeString = "00:00";
        double totalKcal = 0;
        double totalProt = 0;

        for (FoodProfile ingredient: curMeal.mealComposition)
        {
            HashMap<Nutrient, Pair<Double, NutrientMeasurement>> ingredientNutrients = ingredient.nutrition.nutrients;

            totalKcal += ingredientNutrients.getOrDefault(Nutrient.Calorie, new Pair<>(0.0, NutrientMeasurement.g)).first;
            totalProt += ingredientNutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.g)).first;
        }

        String currentMealsTimeAdded = curMeal.timeAdded;

        if (currentMealsTimeAdded.length() >= 5)
        {
            timeString = currentMealsTimeAdded.substring(0, 5);
            timeString = timeString.replaceAll("-", ":");
        }

        String detailString = (int) totalKcal + "kcal | " + (int) totalProt + "p | " + CustomConversionMethods.convertMilitaryTimeToStandardTime(timeString);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.generateRandomColorBasedOffBrand(), holder.imageIconText, curMeal.mealName);

        holder.itemName.setText(curMeal.mealName);
        holder.itemDetails.setText(detailString);
    }

    @Override
    public int getItemCount()
    {
        int mealsListSize = mealsList.size();

        return mealsListSize;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView imageIconText;
        TextView itemDetails;
        CardView imageIcon;
        TextView itemName;
        Button itemInfoBtn;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);

            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemDetails = itemView.findViewById(R.id.listItemDetails);
            imageIcon = itemView.findViewById(R.id.listItemIcon);
            itemName = itemView.findViewById(R.id.listItemName);
            itemInfoBtn = itemView.findViewById(R.id.moreInfoButton);

            itemInfoBtn.setOnClickListener(new View.OnClickListener()
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
        }
    }
}
