package com.example.android.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.adapter.RecipeStepAdapter;
import com.example.android.bakingapp.fragment.StepDetailsFragment;
import com.example.android.bakingapp.fragment.StepListFragment;
import com.example.android.bakingapp.object.Ingredient;
import com.example.android.bakingapp.object.Step;
import com.google.gson.Gson;
import java.util.ArrayList;

import static com.example.android.bakingapp.NavActivity.RECIPE_NAME;
import static com.example.android.bakingapp.fragment.StepDetailsFragment.STEP_LIST;
import static com.example.android.bakingapp.fragment.StepDetailsFragment.STEP_POSITION;
import static com.example.android.bakingapp.fragment.StepListFragment.RECIPE_INGREDIENTS;
import static com.example.android.bakingapp.fragment.StepListFragment.RECIPE_STEPS;

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter.StepItemClickListener, StepDetailsFragment.OnButtonClickListener {

    public static final String INGREDIENTS_LIST = "ingredients_list";
    private static final String STEP_LIST_FRAGMENT = "step_list_fragment";

    private boolean mTwoPane;

    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;
    private String mRecipeName;

    private StepListFragment mStepListFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        retrieveDataFromIntent();
        updateWidgetInformation();

        mFragmentManager = getSupportFragmentManager();

        if(findViewById(R.id.step_details_container) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                mStepListFragment = new StepListFragment();
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

                mStepListFragment.setIngredientList(mIngredientList);
                mStepListFragment.setStepList(mStepList);
                stepDetailsFragment.setStepList(mStepList);
                stepDetailsFragment.setStepPosition(0);

                mFragmentManager.beginTransaction()
                        .add(R.id.step_list_container, mStepListFragment)
                        .commit();

                mFragmentManager.beginTransaction()
                        .add(R.id.step_details_container, stepDetailsFragment)
                        .commit();
            } else {
                mStepListFragment = (StepListFragment) mFragmentManager.getFragment(savedInstanceState, STEP_LIST_FRAGMENT);

                mFragmentManager.beginTransaction()
                        .replace(R.id.step_list_container, mStepListFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;

            if(savedInstanceState == null) {
                mStepListFragment = new StepListFragment();

                mStepListFragment.setIngredientList(mIngredientList);
                mStepListFragment.setStepList(mStepList);

                mFragmentManager.beginTransaction()
                        .add(R.id.step_list_container, mStepListFragment)
                        .commit();
            }
        }

        setTitle(mRecipeName);
    }

    private void updateWidgetInformation() {
        /*
          Code from: https://stackoverflow.com/questions/22984696/storing-array-list-object-in-sharedpreferences/22985657#22985657
          By: Sinan Kozak
         */
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String ingredientsJson = gson.toJson(mIngredientList);

        editor.putString(RECIPE_NAME, mRecipeName);
        editor.putString(INGREDIENTS_LIST, ingredientsJson);

        if (editor.commit()){
            Intent intent = new Intent(this, IngredientsWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            sendBroadcast(intent);
        }
    }

    private void retrieveDataFromIntent() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(RECIPE_INGREDIENTS) && intent.hasExtra(RECIPE_STEPS)) {
            mIngredientList = intent.getParcelableArrayListExtra(RECIPE_INGREDIENTS);
            mStepList = intent.getParcelableArrayListExtra(RECIPE_STEPS);
            mRecipeName = intent.getStringExtra(RECIPE_NAME);
        }
    }

    @Override
    public void onStepItemClick(int clickedStepPosition) {
        if(mTwoPane) {
            replaceDetailsFragment(clickedStepPosition);
        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putParcelableArrayListExtra(STEP_LIST, mStepList);
            intent.putExtra(STEP_POSITION, clickedStepPosition);
            startActivity(intent);
        }
    }

    @Override
    public void onNextPressed(int position) {
        replaceDetailsFragment(position);
        mStepListFragment.onNextPreviousPressed(position);
    }

    @Override
    public void onPreviousPressed(int position) {
        replaceDetailsFragment(position);
        mStepListFragment.onNextPreviousPressed(position);
    }

    private void replaceDetailsFragment(int position) {
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setStepList(mStepList);
        stepDetailsFragment.setStepPosition(position);

        mFragmentManager.beginTransaction()
                .replace(R.id.step_details_container, stepDetailsFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFragmentManager.putFragment(outState, STEP_LIST_FRAGMENT, mStepListFragment);
    }
}
