// Generated code from Butter Knife. Do not modify!
package com.example.android.bakingapp.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.android.bakingapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecipeStepAdapter$RecipeStepViewHolder_ViewBinding implements Unbinder {
  private RecipeStepAdapter.RecipeStepViewHolder target;

  @UiThread
  public RecipeStepAdapter$RecipeStepViewHolder_ViewBinding(RecipeStepAdapter.RecipeStepViewHolder target,
      View source) {
    this.target = target;

    target.listItemShortDescView = Utils.findRequiredViewAsType(source, R.id.tv_recipe_step_short_description, "field 'listItemShortDescView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RecipeStepAdapter.RecipeStepViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listItemShortDescView = null;
  }
}
