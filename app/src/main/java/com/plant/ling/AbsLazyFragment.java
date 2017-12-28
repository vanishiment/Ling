package com.plant.ling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbsLazyFragment extends Fragment {

  private boolean mIsUIPrepared;
  private boolean mIsDataInited;

  private Unbinder mUnbinder;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View mRootView = inflater.inflate(getLayoutId(), container, false);
    mIsUIPrepared = true;
    mUnbinder = ButterKnife.bind(this,mRootView);
    lazyLoad();
    return mRootView;
  }

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser) lazyLoad();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    mUnbinder.unbind();
  }

  private void lazyLoad() {
    if (getUserVisibleHint() && mIsUIPrepared && !mIsDataInited) loadData();
  }

  public abstract int getLayoutId();

  public abstract void setupView();

  /**
   * 数据加载完成调用 loadDataFinished
   */
  public abstract void loadData();

  public void refreshData() {
    mIsDataInited = false;
    loadData();
  }

  public void loadDataFinish() {
    mIsDataInited = true;
  }
}
