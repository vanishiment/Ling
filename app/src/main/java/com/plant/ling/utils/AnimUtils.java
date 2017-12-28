package com.plant.ling.utils;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public final class AnimUtils {

  private static Interpolator fastOutSlowIn;

  private AnimUtils() {
  }

  public static Interpolator getFastOutSlowInInterpolator(Context context) {
    if (fastOutSlowIn == null) {
      fastOutSlowIn =
          AnimationUtils.loadInterpolator(context, android.R.interpolator.fast_out_slow_in);
    }
    return fastOutSlowIn;
  }
}
