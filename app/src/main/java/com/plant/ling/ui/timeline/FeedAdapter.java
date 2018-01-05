package com.plant.ling.ui.timeline;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.plant.ling.R;
import com.plant.ling.data.model.Feed;
import com.plant.ling.ui.detail.DetailActivity;
import com.plant.ling.ui.widget.QuickAdapter;
import com.plant.ling.utils.Objects;
import com.plant.ling.utils.schedulers.SchedulerProvider;
import io.reactivex.Observable;
import java.util.Arrays;
import java.util.List;

public class FeedAdapter extends QuickAdapter<Feed> {

  static final int REQUEST_CODE_VIEW_SHOT = 5407;

  private int dataVersion = 0;

  private Activity mContext;

  public FeedAdapter(Activity context, List<Feed> dataList) {
    super(dataList);
    mContext = context;
  }

  @Override public int getLayoutId(int viewType) {
    return R.layout.feed_item;
  }

  @Override public void bindVH(VH holder, Feed data, int position) {
    if (data == null) return;
    TextView type = holder.getView(R.id.type_tv);
    TextView title = holder.getView(R.id.title_tv);
    TextView content = holder.getView(R.id.content_tv);

    if (!TextUtils.isEmpty(data.type)) type.setText(data.type);
    if (!TextUtils.isEmpty(data.title)) title.setText(data.title);
    if (!TextUtils.isEmpty(data.desc)) content.setText(data.desc);

    holder.getView(R.id.feed_item_cl).setOnClickListener(v -> {
      Intent intent = new Intent(mContext, DetailActivity.class);
      intent.putExtra(DetailActivity.DETAIL_WEB_URL_ID, data.id);
      intent.putExtra(DetailActivity.DETAIL_IMG_URL, data.thumbnailUrl);
      intent.putExtra(DetailActivity.DETAIL_TITLE, data.title);
      List<Pair<View, String>> pairList = Arrays.asList(Pair.create(v, mContext.getString(R.string.transition_shot)),
          Pair.create(v, mContext.getString(R.string.transition_shot_background)));
      ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
          (Pair<View, String>[]) pairList.toArray());
      mContext.startActivityForResult(intent, REQUEST_CODE_VIEW_SHOT, options.toBundle());
    });
  }

  public void replaceData(List<Feed> feeds) {
    dataVersion++;

    if (mDataList == null) {
      if (feeds == null) return;
      mDataList = feeds;
      notifyDataSetChanged();
    } else if (feeds == null) {
      int oldSize = mDataList.size();
      mDataList = null;
      notifyItemRangeChanged(0, oldSize);
    } else {
      final int startVersion = dataVersion;
      final List<Feed> oldItems = mDataList;
      Observable.just("1")
          .subscribeOn(SchedulerProvider.get().io())
          .observeOn(SchedulerProvider.get().ui())
          .map(s -> DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() {
              return oldItems.size();
            }

            @Override public int getNewListSize() {
              return feeds.size();
            }

            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
              Feed oldItem = oldItems.get(oldItemPosition);
              Feed newItem = feeds.get(newItemPosition);
              return Objects.equals(oldItem.id, newItem.id) && Objects.equals(oldItem.detailUrl,
                  newItem.detailUrl);
            }

            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
              Feed oldItem = oldItems.get(oldItemPosition);
              Feed newItem = feeds.get(newItemPosition);
              return Objects.equals(oldItem.desc, newItem.desc) && Objects.equals(oldItem.title,
                  newItem.title);
            }
          }))
          .subscribe(
              // onNext
              diffResult -> {
                if (startVersion != dataVersion) {
                  // ignore update
                  return;
                }
                mDataList = feeds;
                diffResult.dispatchUpdatesTo(FeedAdapter.this);
              });
    }
  }

  public void addData(List<Feed> feeds) {
    setDataList(feeds);
    notifyDataSetChanged();
  }
}
