package com.example.android.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.loader.RecipeLoader;
import com.example.android.bakingapp.object.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.android.bakingapp.fragment.StepListFragment.RECIPE_INGREDIENTS;
import static com.example.android.bakingapp.fragment.StepListFragment.RECIPE_STEPS;

public class NavActivity extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener, LoaderManager.LoaderCallbacks<List<Recipe>> {


    public static final String RECIPE_NAME = "recipe_name";
    private static final String RECIPE_REQUEST_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final int RECIPES_LOADER_ID = 1;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @BindView(R.id.loading_spinner)
    ProgressBar loadingSpinner;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.rv_recipe_list) RecyclerView mRecipeList;
    private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        if(this.getResources().getConfiguration().screenWidthDp >= 600){
            mRecipeList.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecipeList.setLayoutManager(new LinearLayoutManager(this));
        }

        mRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeAdapter(this);
        mRecipeList.setAdapter(mAdapter);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        LoaderManager loaderManager = getLoaderManager();

        if(isConnected) {
            loaderManager.initLoader(RECIPES_LOADER_ID, null, this);
        } else {
            loadingSpinner.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet);
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRecipeItemClick(Recipe clickedRecipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putParcelableArrayListExtra(RECIPE_INGREDIENTS, clickedRecipe.getIngredients());
        intent.putParcelableArrayListExtra(RECIPE_STEPS, clickedRecipe.getSteps());
        intent.putExtra(RECIPE_NAME, clickedRecipe.getName());
        startActivity(intent);
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int i, Bundle bundle) {
        return new RecipeLoader(this, RECIPE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipes) {
        mAdapter.resetRecipeData();

        if (recipes != null && !recipes.isEmpty()) {
            mAdapter.setRecipeData((ArrayList<Recipe>) recipes);
        }
        mRecipeList.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.resetRecipeData();
    }
}
