// Generated code from Butter Knife. Do not modify!
package com.example.android.bakingapp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.android.bakingapp.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StepDetailsFragment_ViewBinding implements Unbinder {
  private StepDetailsFragment target;

  @UiThread
  public StepDetailsFragment_ViewBinding(StepDetailsFragment target, View source) {
    this.target = target;

    target.stepDescription = Utils.findRequiredViewAsType(source, R.id.tv_step_description, "field 'stepDescription'", TextView.class);
    target.nextStepButton = Utils.findRequiredViewAsType(source, R.id.next_step_button, "field 'nextStepButton'", Button.class);
    target.previousStepButton = Utils.findRequiredViewAsType(source, R.id.previous_step_button, "field 'previousStepButton'", Button.class);
    target.mPlayerView = Utils.findRequiredViewAsType(source, R.id.step_video_view, "field 'mPlayerView'", SimpleExoPlayerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StepDetailsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.stepDescription = null;
    target.nextStepButton = null;
    target.previousStepButton = null;
    target.mPlayerView = null;
  }
}
