package com.udacity.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.StepAdapter;
import com.udacity.bakingapp.fragments.RecipeDetailsFragment;
import com.udacity.bakingapp.fragments.RecipeStepsFragment;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.model.Step;

import java.util.List;

import static com.udacity.bakingapp.utils.Constants.RECIPE_DETAILS_STEPS;
import static com.udacity.bakingapp.utils.Constants.RECIPE_INTENT_EXTRA;

public class DetailActivity extends AppCompatActivity implements StepAdapter.ItemClickListener {

    private final static String TAG = DetailActivity.class.getSimpleName();

    Recipe mRecipe;
    List<Step> mStepList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra(RECIPE_INTENT_EXTRA);

        mStepList = mRecipe.getSteps();

        if (savedInstanceState == null) {
            RecipeDetailsFragment detailsFragment = new RecipeDetailsFragment();

            Bundle args = new Bundle();
            args.putParcelable(RECIPE_DETAILS_STEPS, mRecipe);
            detailsFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_content, detailsFragment)
                    .commit();

            if (findViewById(R.id.detail_container_tablet) != null) {

                Step step = mStepList.get(0);

                RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
                Bundle stepArgs = new Bundle();
                stepArgs.putParcelable(RECIPE_DETAILS_STEPS, step);
                recipeStepsFragment.setArguments(stepArgs);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container_step_detail, recipeStepsFragment)
                        .commit();
            }

        }
    }


    @Override
    public void onListItemClick(List<Step> stepList, int itemIndex) {
        Step listStep = stepList.get(itemIndex);

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_DETAILS_STEPS, listStep);
        recipeStepsFragment.setArguments(args);

        if (findViewById(R.id.detail_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_content, recipeStepsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_step_detail, recipeStepsFragment)
                    .addToBackStack(null)
                    .commit();
        }


    }
}
