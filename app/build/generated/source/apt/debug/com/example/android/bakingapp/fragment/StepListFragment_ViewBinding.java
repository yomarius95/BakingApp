// Generated code from Butter Knife. Do not modify!
package com.example.android.bakingapp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.android.bakingapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StepListFragment_ViewBinding implements Unbinder {
  private StepListFragment target;

  @UiThread
  public StepListFragment_ViewBinding(StepListFragment target, View source) {
    this.target = target;

    target.mRecipeStepRecyclerView = Utils.findRequiredViewAsType(source, R.id.rv_recipe_step_list, "field 'mRecipeStepRecyclerView'", RecyclerView.class);
    target.mRecipeIngredients = Utils.findRequiredViewAsType(source, R.id.tv_recipe_ingredients, "field 'mRecipeIngredients'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StepListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecipeStepRecyclerView = null;
    target.mRecipeIngredients = null;
  }
}
