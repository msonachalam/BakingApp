package com.udacity.bakingapp.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.activities.DetailActivity;
import com.udacity.bakingapp.adapter.IngredientAdapter;
import com.udacity.bakingapp.adapter.StepAdapter;
import com.udacity.bakingapp.model.Ingredient;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;
import com.udacity.bakingapp.widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.bakingapp.utils.Constants.RECIPE_DETAILS_STEPS;
import static com.udacity.bakingapp.utils.Constants.STATE_INGREDIENT;
import static com.udacity.bakingapp.utils.Constants.STATE_RECIPE;
import static com.udacity.bakingapp.utils.Constants.STATE_STEP;

public class RecipeDetailsFragment extends Fragment {

    private final static String TAG = RecipeDetailsFragment.class.getSimpleName();


    @BindView(R.id.ingredient_recycler_view)
    RecyclerView ingredientRecyclerView;
    @BindView(R.id.step_recycler_view)
    RecyclerView stepRecyclerView;

    Recipe mRecipe;

    List<Ingredient> mIngredientList = null;
    List<Step> mStepList = null;

    LinearLayoutManager mLayoutManager;
    LinearLayoutManager mLinearLayoutManager;

    IngredientAdapter ingredientAdapter;
    StepAdapter stepAdapter;

    Parcelable ingredientListState;
    Parcelable stepListState;

    public RecipeDetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(STATE_RECIPE);
            ingredientListState = savedInstanceState.getParcelable(STATE_INGREDIENT);
            stepListState = savedInstanceState.getParcelable(STATE_STEP);
        } else if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(RECIPE_DETAILS_STEPS);
        }

        if (mRecipe != null) {
            mIngredientList = mRecipe.getIngredients();
            mStepList = mRecipe.getSteps();
        }

        ingredientAdapter = new IngredientAdapter(mIngredientList);
        stepAdapter = new StepAdapter(mStepList, (DetailActivity) getActivity());

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());

        ingredientRecyclerView.setLayoutManager(mLinearLayoutManager);
        stepRecyclerView.setLayoutManager(mLayoutManager);

        ingredientRecyclerView.setAdapter(ingredientAdapter);
        stepRecyclerView.setAdapter(stepAdapter);

        final ArrayList<String> recipeIngredientForWidgets = new ArrayList<>();

        for (int i = 0; i < mIngredientList.size(); i++) {

            String ingredientName = mIngredientList.get(i).getIngredient();
            Double quantity = mIngredientList.get(i).getQuantity();
            String measure = mIngredientList.get(i).getMeasure();

            recipeIngredientForWidgets.add(ingredientName + "\n" + "Quantity: " + quantity + "\n" + "Measure: " + measure + "\n");
        }

        UpdateWidgetService.startBakingService(getContext(), recipeIngredientForWidgets);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_RECIPE, mRecipe);
        outState.putParcelable(STATE_INGREDIENT, ingredientRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelable(STATE_STEP, stepRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ingredientListState != null)
            ingredientRecyclerView.getLayoutManager().onRestoreInstanceState(ingredientListState);

        if (stepListState != null)
            stepRecyclerView.getLayoutManager().onRestoreInstanceState(stepListState);
    }
}
