package com.udacity.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.activities.DetailActivity;
import com.udacity.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.bakingapp.utils.Constants.JSON_RESULT_EXTRA;
import static com.udacity.bakingapp.utils.Constants.RECIPE_INTENT_EXTRA;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context mContext;
    private final List<Recipe> mRecipeList;
    private String mJsonResult;
    String recipeJson;

    public RecipeAdapter(Context context, List<Recipe> recipeList, String jsonResult) {
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mJsonResult = jsonResult;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder recipeViewHolder, int position) {

        Recipe recipe = mRecipeList.get(position);

        String imageUrl = recipe.getImage();

        if (!"".equals(imageUrl)) {
            Uri uri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.get().load(uri).into(recipeViewHolder.recipeIcon);
        }

        recipeViewHolder.recipeName.setText(recipe.getName());

        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe1 = mRecipeList.get(recipeViewHolder.getAdapterPosition());
                recipeJson = jsonToString(mJsonResult, recipeViewHolder.getAdapterPosition());
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(RECIPE_INTENT_EXTRA, recipe1);
                intent.putExtra(JSON_RESULT_EXTRA, recipeJson);
                mContext.startActivity(intent);
            }
        });
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView recipeName;

        @Nullable
        @BindView(R.id.iv_recipe)
        ImageView recipeIcon;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String jsonToString(String jsonResult, int position) {
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }
}
