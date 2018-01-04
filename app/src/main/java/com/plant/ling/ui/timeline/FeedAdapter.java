package com.plant.ling.ui.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.plant.ling.R;
import com.plant.ling.ui.detail.DetailActivity;
import com.plant.ling.ui.widget.QuickAdapter;
import com.plant.ling.data.model.Feed;
import com.plant.ling.utils.Objects;
import java.util.List;

public class FeedAdapter extends QuickAdapter<Feed> {

  private int dataVersion = 0;

  private Context mContext;

  public FeedAdapter(Context context,List<Feed> dataList) {
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

    holder.getView(R.id.feed_item_cl).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(DetailActivity.DETAIL_WEB_URL_ID,data.id);
        intent.putExtra(DetailActivity.DETAIL_IMG_URL,data.thumbnailUrl);
        intent.putExtra(DetailActivity.DETAIL_TITLE,data.title);
        mContext.startActivity(intent);
      }
    });
  }

  public void replaceData(List<Feed> feeds){
    dataVersion++;

    if (mDataList == null){
      if (feeds == null) return;
      mDataList = feeds;
      notifyDataSetChanged();
    }else if (feeds == null){
      int oldSize = mDataList.size();
      mDataList = null;
      notifyItemRangeChanged(0,oldSize);
    }else {
      final int startVersion = dataVersion;
      final List<Feed> oldItems = mDataList;
      new AsyncTask<Void,Void,DiffUtil.DiffResult>(){

        @Override protected DiffUtil.DiffResult doInBackground(Void... voids) {
          return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override public int getOldListSize() {
              return oldItems.size();
            }

            @Override public int getNewListSize() {
              return feeds.size();
            }

            @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
              Feed oldItem = oldItems.get(oldItemPosition);
              Feed newItem = feeds.get(newItemPosition);
              return Objects.equals(oldItem.id,newItem.id) && Objects.equals(oldItem.detailUrl,newItem.detailUrl);
            }

            @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
              Feed oldItem = oldItems.get(oldItemPosition);
              Feed newItem = feeds.get(newItemPosition);
              return Objects.equals(oldItem.desc,newItem.desc) && Objects.equals(oldItem.title,newItem.title);
            }
          });
        }

        @Override protected void onPostExecute(DiffUtil.DiffResult diffResult) {
          if (startVersion != dataVersion) {
            // ignore update
            return;
          }
          mDataList = feeds;
          diffResult.dispatchUpdatesTo(FeedAdapter.this);
        }
      }.execute();}
  }

  public void addData(List<Feed> feeds){
    setDataList(feeds);
    notifyDataSetChanged();
  }
}
