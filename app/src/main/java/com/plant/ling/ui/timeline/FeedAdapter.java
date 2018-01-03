package com.plant.ling.ui.timeline;

import com.plant.ling.R;
import com.plant.ling.ui.widget.QuickAdapter;
import com.plant.ling.data.model.Feed;
import java.util.List;

public class FeedAdapter extends QuickAdapter<Feed> {

  public FeedAdapter(List<Feed> dataList) {
    super(dataList);
  }

  @Override public int getLayoutId(int viewType) {
    return R.layout.feed_item;
  }

  @Override public void bindVH(VH holder, Feed data, int position) {

  }

  public void replaceData(List<Feed> feeds){
    setDataList(feeds);
    notifyDataSetChanged();
  }

  public void addData(List<Feed> feeds){
    setDataList(feeds);
    notifyDataSetChanged();
  }
}
