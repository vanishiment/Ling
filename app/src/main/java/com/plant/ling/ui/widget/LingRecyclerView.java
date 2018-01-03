package com.plant.ling.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class LingRecyclerView extends RecyclerView {

  private FrameLayout mStateLayout;

  private AdapterDataObserver mObserver = new AdapterDataObserver() {
    @Override public void onChanged() {
      if (mStateLayout != null){
        Adapter adapter = getAdapter();
        if (adapter.getItemCount() == 0){
          mStateLayout.setVisibility(VISIBLE);
          LingRecyclerView.this.setVisibility(GONE);
        }else {
          mStateLayout.setVisibility(GONE);
          LingRecyclerView.this.setVisibility(VISIBLE);
        }
      }
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
      onChanged();
    }

    @Override public void onItemRangeInserted(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
      onChanged();
    }

    @Override public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
      onChanged();
    }
  };

  public LingRecyclerView(Context context) {
    super(context);
  }

  public LingRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public LingRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setStateLayout(FrameLayout frameLayout){
    this.mStateLayout = frameLayout;
    ((ViewGroup)getRootView()).addView(mStateLayout);
  }

  public void setAdapter(Adapter adapter){
    super.setAdapter(adapter);
    adapter.registerAdapterDataObserver(mObserver);
    mObserver.onChanged();
  }
}
