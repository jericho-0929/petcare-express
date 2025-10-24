package com.example.petcareexpress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcareexpress.objects.FoodData;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private final List<FoodData> foodDataList;
    public FoodAdapter(List<FoodData> foodDataList) {
        this.foodDataList = foodDataList;
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.select_food_item_row, viewGroup, false);
        return new FoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int position) {
        FoodData currentItem = foodDataList.get(position);

        foodViewHolder.textViewName.setText(currentItem.name);
        foodViewHolder.textViewType.setText(currentItem.foodType);
        foodViewHolder.textViewMinimumPetWeight.setText(String.valueOf(currentItem.minimumPetWeight));
        foodViewHolder.textViewMinimumFeedAmount.setText(String.valueOf(currentItem.minimumFeedingAmount));
    }
    @Override
    public int getItemCount() {
        return foodDataList.size();
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
        private TextView textViewMinimumFeedAmount;
        private TextView textViewMinimumPetWeight;
        public FoodViewHolder(@NonNull View view) {
            super(view);
            textViewName = view.findViewById(R.id.food_row_name);
            textViewType = view.findViewById(R.id.food_row_type);
            textViewMinimumFeedAmount = view.findViewById(R.id.food_row_min_feed_amount);
            textViewMinimumPetWeight = view.findViewById(R.id.food_row_min_pet_weight);
        }
    }
}
