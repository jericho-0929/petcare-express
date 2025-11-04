package com.example.petcareexpress;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petcareexpress.objects.FoodData;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private final List<FoodData> foodDataList;
    private FoodData selectedItem;
    private int recyclerViewPosition = RecyclerView.NO_POSITION;
    public FoodAdapter(List<FoodData> foodDataList, RecyclerView recyclerView) {
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
        FoodData itemToRender = foodDataList.get(position);

        foodViewHolder.textViewName.setText(itemToRender.name);
        foodViewHolder.textViewType.setText(itemToRender.foodType);
        foodViewHolder.textViewMinimumPetWeight.setText(String.valueOf(itemToRender.minimumPetWeight));
        foodViewHolder.textViewMinimumFeedAmount.setText(String.valueOf(itemToRender.minimumFeedingAmount));


        if (foodViewHolder.getBindingAdapterPosition() == recyclerViewPosition) {
            // Set to specified color to denote it is selected.
            foodViewHolder.cardView.setCardBackgroundColor(Color.GREEN);
        } else {
            // Set to gray to denote deselection.
            foodViewHolder.cardView.setCardBackgroundColor(Color.GRAY);
        }

        foodViewHolder.itemView.setOnClickListener(v -> {
            // Current Item is currently the previous item at this line.
            if (selectedItem != null) {
                selectedItem.setSelected(false);
            }

            // Change currentItem to
            recyclerViewPosition = foodViewHolder.getBindingAdapterPosition();
            selectedItem = foodDataList.get(foodViewHolder.getBindingAdapterPosition());
            selectedItem.setSelected(true);

            Toast.makeText(foodViewHolder.itemView.getContext(), selectedItem.name + " selected.", Toast.LENGTH_SHORT).show();
            notifyItemRangeChanged(0, foodDataList.size());
        });
    }
    @Override
    public int getItemCount() {
        return foodDataList.size();
    }
    public FoodData getCurrentFoodData() {
        return selectedItem;
    }
    public int getCurrentIndex() {
        return recyclerViewPosition;
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewType;
        private final TextView textViewMinimumFeedAmount;
        private final TextView textViewMinimumPetWeight;
        private final CardView cardView;
        public FoodViewHolder(@NonNull View view) {
            super(view);
            textViewName = view.findViewById(R.id.food_row_name);
            textViewType = view.findViewById(R.id.food_row_type);
            textViewMinimumFeedAmount = view.findViewById(R.id.food_row_min_feed_amount);
            textViewMinimumPetWeight = view.findViewById(R.id.food_row_min_pet_weight);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
