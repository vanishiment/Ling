package com.plant.ling;

import java.util.List;

public class FeedAdapter extends QuickAdapter<Feed> {

  public FeedAdapter(List<Feed> dataList) {
    super(dataList);
  }

  @Override public int getLayoutId(int viewType) {
    return 0;
  }

  @Override public void bindVH(VH holder, Feed data, int position) {

  }

  public int getItemColumnSpan(){
    return 0;
  }
}
