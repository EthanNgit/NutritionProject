package com.example.nutritionproject.Custom.java.Custom.UI;

import android.content.Context;
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

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.MyViewHolder>
{
    private final RecyclerViewInterface recyclerViewInterface;
    ArrayList<FoodProfile> profileList;
    Context context;


    public IngredientListAdapter(Context context, ArrayList<FoodProfile> profileList, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.profileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_list_view, parent, false);
        IngredientListAdapter.MyViewHolder ingredientViewHolder = new IngredientListAdapter.MyViewHolder(view, recyclerViewInterface);

        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientListAdapter.MyViewHolder holder, int position)
    {
        FoodProfile curProfile = profileList.get(position);

        CustomUIMethods.setProfileButton(context, holder.imageIcon, CustomUIMethods.generateRandomColorBasedOffBrand(), holder.imageIconText, curProfile.name);

        String detailString = (int) curProfile.nutrition.nutrients.getOrDefault(Nutrient.Calorie, new Pair<>(0.0, NutrientMeasurement.none)).first.intValue()
                + " kcal | " + curProfile.nutrition.nutrients.getOrDefault(Nutrient.Protein, new Pair<>(0.0, NutrientMeasurement.g)).first.intValue() + " p";

        holder.itemName.setText(curProfile.name);
        holder.itemDetails.setText(detailString);
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
        TextView itemDetails;
        ImageView itemTrash;
        CardView imageIcon;
        TextView itemName;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface)
        {
            super(itemView);

            imageIconText = itemView.findViewById(R.id.listItemIconText);
            itemTrash = itemView.findViewById(R.id.listItemTrash);
            itemDetails = itemView.findViewById(R.id.listItemDetails);
            imageIcon = itemView.findViewById(R.id.listItemIcon);
            itemName = itemView.findViewById(R.id.listItemName);

            itemTrash.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
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
