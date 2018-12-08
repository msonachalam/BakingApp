package com.udacity.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> mIngredients;

    public void setData(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public IngredientAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.ingredient_list_item, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);

        IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;

        ingredientViewHolder.ingredientTextView.setText(ingredient.getIngredient());

        String quantity = String.valueOf(ingredient.getQuantity());
        ingredientViewHolder.quantityTextView.setText(quantity);

        ingredientViewHolder.measureTextView.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        if (mIngredients != null) {
            return mIngredients.size();
        }
        return 0;
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_ingredient)
        TextView ingredientTextView;

        @Nullable
        @BindView(R.id.tv_ingredient_quantity)
        TextView quantityTextView;

        @Nullable
        @BindView(R.id.tv_ingredient_unit)
        TextView measureTextView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
