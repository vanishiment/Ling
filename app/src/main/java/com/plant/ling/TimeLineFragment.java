package com.plant.ling;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindInt;
import butterknife.BindView;

public class TimeLineFragment extends AbsLazyFragment implements
    SwipeRefreshLayout.OnRefreshListener{

  @BindView(R.id.time_line_swipe_refresh_layout) SwipeRefreshLayout mSRL;
  @BindView(R.id.time_line_recycler_view) LingRecyclerView mRV;

  @BindInt(R.integer.time_line_grid_columns) int mColumns;

  public TimeLineFragment() {
    // Required empty public constructor
  }

  public static TimeLineFragment newInstance() {
    return new TimeLineFragment();
  }

  @Override public int getLayoutId() {
    return R.layout.fragment_time_line;
  }

  @Override public void setupView() {
    mSRL.setColorSchemeResources(R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorAccent);
    mSRL.setOnRefreshListener(this);

    final FeedAdapter feedAdapter = new FeedAdapter(null);
    mRV.setAdapter(feedAdapter);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),mColumns);
    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return feedAdapter.getItemColumnSpan();
      }
    });
    mRV.setLayoutManager(gridLayoutManager);
    mRV.setHasFixedSize(true);
    mRV.addItemDecoration(new GridItemDividerDecoration(getActivity(),R.dimen.divider_height,R.color.divider));
    mRV.setItemAnimator(new SlideInItemAnimator());
    mRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // 1.下拉或者正在加载数据不加载
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (totalItemCount - visibleItemCount <= firstVisibleItem + 5){
          recyclerView.post(new Runnable() {
            @Override public void run() {
              loadMore();
            }
          });
        }
      }
    });
  }

  @Override public void loadData() {

  }

  private void loadMore(){}

  @Override public void onRefresh() {
    mSRL.setRefreshing(true);
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        mSRL.setRefreshing(false);
      }
    },3000);
  }
}
