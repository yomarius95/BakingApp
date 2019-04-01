package com.example.android.bakingapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeActivity;
import com.example.android.bakingapp.adapter.RecipeStepAdapter;
import com.example.android.bakingapp.object.Ingredient;
import com.example.android.bakingapp.object.Step;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {

    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String RECIPE_STEPS = "recipe_steps";
    private static final String ADAPTER_SELECTED_POSITION = "adapter_selected_position";

    @BindView(R.id.rv_recipe_step_list)
    RecyclerView mRecipeStepRecyclerView;
    @BindView(R.id.tv_recipe_ingredients)
    TextView mRecipeIngredients;
    private RecipeStepAdapter mAdapter;
    private int mAdapterSelectedPos;

    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;

    public StepListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);
        ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList(RECIPE_STEPS);
            mIngredientList = savedInstanceState.getParcelableArrayList(RECIPE_INGREDIENTS);
            mAdapterSelectedPos = savedInstanceState.getInt(ADAPTER_SELECTED_POSITION);
        }

        setIngredientsTextView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecipeStepRecyclerView.setLayoutManager(layoutManager);

        mRecipeStepRecyclerView.setHasFixedSize(true);

        mAdapter = new RecipeStepAdapter((RecipeActivity)getActivity());
        mAdapter.setRecipeStepData(mStepList);
        mAdapter.setSelectedPos(mAdapterSelectedPos);
        mRecipeStepRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void setIngredientsTextView() {
        StringBuilder ingredientsText = new StringBuilder();
        for (int i = 0; i < mIngredientList.size(); i++) {
            Ingredient ingredient = mIngredientList.get(i);
            DecimalFormat format = new DecimalFormat("0.#");
            String text = (i+1) + ". " + format.format(ingredient.getQuantity()) + " " + ingredient.getMeasure() + " of " + ingredient.getName() + "\n";
            ingredientsText.append(text);
        }
        mRecipeIngredients.setText(ingredientsText.toString());
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.mStepList = stepList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_STEPS, mStepList);
        outState.putParcelableArrayList(RECIPE_INGREDIENTS, mIngredientList);
        outState.putInt(ADAPTER_SELECTED_POSITION, mAdapter.getSelectedPos());
    }

    public void onNextPreviousPressed (int position) {
        mAdapter.notifyItemChanged(mAdapter.getSelectedPos());
        mAdapter.setSelectedPos(position);
        mAdapter.notifyItemChanged(position);
    }
}
