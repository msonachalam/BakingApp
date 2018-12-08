package com.udacity.bakingapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.SimpleIdlingResource.SimpleIdlingResource;
import com.udacity.bakingapp.fragments.RecipeFragment;

import static com.udacity.bakingapp.utils.Constants.IS_TABLET;

public class RecipeActivity extends AppCompatActivity {

    private final String TAG = RecipeActivity.class.getSimpleName();

    @Nullable
    private SimpleIdlingResource mSimpleIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mSimpleIdlingResource == null) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeFragment recipeFragment = new RecipeFragment();
        Bundle args = new Bundle();

        if (findViewById(R.id.recipe_container) != null) {
            args.putBoolean(IS_TABLET, false);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_container, recipeFragment)
                    .commit();
        } else {
            args.putBoolean(IS_TABLET, true);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_container_land, recipeFragment)
                    .commit();
        }

        getIdlingResource();
    }
}
