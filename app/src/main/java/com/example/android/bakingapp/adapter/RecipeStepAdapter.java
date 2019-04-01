package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.object.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private ArrayList<Step> mRecipeSteps;

    final private StepItemClickListener mOnClickListener;

    private int selectedPos = 0;

    public interface StepItemClickListener {
        void onStepItemClick(int clickedStepPosition);
    }

    public RecipeStepAdapter(StepItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recipe_step_list_item, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
    }


    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_recipe_step_short_description) TextView listItemShortDescView;

        RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemShortDescView.setText(mRecipeSteps.get(listIndex).getShortDesc());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onStepItemClick(clickedPosition);
            Timber.i(String.valueOf(clickedPosition));
            // Code from: https://stackoverflow.com/a/30046476/8933681
            notifyItemChanged(selectedPos);
            selectedPos = clickedPosition;
            notifyItemChanged(selectedPos);
        }
    }

    public void setRecipeStepData(ArrayList<Step> recipeStepData) {
        mRecipeSteps = recipeStepData;
        notifyDataSetChanged();
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }
}
