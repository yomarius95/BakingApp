package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.object.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> mRecipes;

    final private RecipeItemClickListener mOnClickListener;

    public interface RecipeItemClickListener {
        void onRecipeItemClick(Recipe clickedRecipe);
    }

    public RecipeAdapter(RecipeItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_recipe_name) TextView listItemNameView;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemNameView.setText(mRecipes.get(listIndex).getName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClick(mRecipes.get(clickedPosition));
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipeData) {
        mRecipes = recipeData;
        notifyDataSetChanged();
    }

    public void resetRecipeData() {
        mRecipes = null;
    }
}
