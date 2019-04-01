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

public class RecipeAdapter$RecipeViewHolder_ViewBinding implements Unbinder {
  private RecipeAdapter.RecipeViewHolder target;

  @UiThread
  public RecipeAdapter$RecipeViewHolder_ViewBinding(RecipeAdapter.RecipeViewHolder target,
      View source) {
    this.target = target;

    target.listItemNameView = Utils.findRequiredViewAsType(source, R.id.tv_recipe_name, "field 'listItemNameView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RecipeAdapter.RecipeViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listItemNameView = null;
  }
}
