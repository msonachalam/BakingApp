package com.udacity.bakingapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.adapter.RecipeAdapter;
import com.udacity.bakingapp.model.Recipe;
import com.udacity.bakingapp.rest.RecipeClient;
import com.udacity.bakingapp.rest.RecipeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.bakingapp.utils.Constants.IS_TABLET;

public class RecipeFragment extends Fragment {

    private final static String TAG = RecipeFragment.class.getSimpleName();
    RecipeService mRecipeService;
    RecipeAdapter recipeAdapter;
    List<Recipe> mRecipeArrayList = new ArrayList<>();

    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    String mJsonResponse;
    boolean isTablet;

    @BindView(R.id.progress_bar)
    ProgressBar loading;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    public RecipeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_recipes, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            isTablet = getArguments().getBoolean(IS_TABLET);
        }

        if (!isTablet) {
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        if (isNetworkAvailable()) {
            mRecipeService = new RecipeClient().mRecipeService;
            new FetchRecipesAsync().execute();
        } else
            Toast.makeText(getContext(), getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();

    }

    private class FetchRecipesAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            fetchRecipes();
            return null;
        }
    }

    private void fetchRecipes() {
        Call<List<Recipe>> call = mRecipeService.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                Integer statusCode = response.code();
                Log.d(TAG, "status code: " + statusCode.toString());
                mRecipeArrayList = response.body();
                Log.d(TAG, "Size: " + mRecipeArrayList.size());
                mJsonResponse = new Gson().toJson(response.body());

                recipeAdapter = new RecipeAdapter(getActivity(), mRecipeArrayList, mJsonResponse);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
