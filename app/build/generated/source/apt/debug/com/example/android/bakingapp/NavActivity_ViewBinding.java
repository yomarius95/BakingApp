// Generated code from Butter Knife. Do not modify!
package com.example.android.bakingapp;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NavActivity_ViewBinding implements Unbinder {
  private NavActivity target;

  @UiThread
  public NavActivity_ViewBinding(NavActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NavActivity_ViewBinding(NavActivity target, View source) {
    this.target = target;

    target.loadingSpinner = Utils.findRequiredViewAsType(source, R.id.loading_spinner, "field 'loadingSpinner'", ProgressBar.class);
    target.emptyView = Utils.findRequiredViewAsType(source, R.id.empty_view, "field 'emptyView'", TextView.class);
    target.mRecipeList = Utils.findRequiredViewAsType(source, R.id.rv_recipe_list, "field 'mRecipeList'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NavActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.loadingSpinner = null;
    target.emptyView = null;
    target.mRecipeList = null;
  }
}
