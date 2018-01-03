package com.plant.ling.ui.timeline;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindInt;
import butterknife.BindView;
import com.plant.ling.data.model.Feed;
import com.plant.ling.ui.base.AbsLazyFragment;
import com.plant.ling.ui.widget.GridItemDividerDecoration;
import com.plant.ling.ui.widget.LingRecyclerView;
import com.plant.ling.R;
import com.plant.ling.ui.widget.SlideInItemAnimator;
import java.util.ArrayList;
import java.util.List;

public class TimeLineFragment extends AbsLazyFragment implements
    SwipeRefreshLayout.OnRefreshListener,FeedsContract.View{

  private FeedsContract.Presenter mPresenter;

  @BindView(R.id.time_line_swipe_refresh_layout) SwipeRefreshLayout mSRL;
  @BindView(R.id.time_line_recycler_view) LingRecyclerView mRV;

  @BindView(R.id.state_fl) FrameLayout mStateFL;
  @BindView(R.id.empty_tv) TextView mEmptyTV;
  @BindView(R.id.error_layout) RelativeLayout mErrorRL;
  @BindView(R.id.error_tv) TextView mErrorTV;
  @BindView(R.id.error_btn) Button mErrorBtn;

  private FeedAdapter feedAdapter;

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

    mRV.setAdapter(feedAdapter);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
    mRV.setLayoutManager(linearLayoutManager);
    mRV.setHasFixedSize(true);
    mRV.addItemDecoration(new DividerItemDecoration(getActivity(),linearLayoutManager.getOrientation()));
    mRV.setItemAnimator(new SlideInItemAnimator());
    mRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // 1.下拉或者正在加载数据不加载
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
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
    mPresenter.subscribe();
  }

  private void loadMore(){
    mPresenter.loadMoreFeeds(true,30,30);
  }

  @Override public void onRefresh() {
    mPresenter.loadFeeds(false,0,30);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    feedAdapter = new FeedAdapter(new ArrayList<>(0));
  }

  @Override public void onResume() {
    super.onResume();

  }

  @Override public void onPause() {
    super.onPause();
    mPresenter.unSubscribe();
  }

  @Override public void setPresenter(FeedsContract.Presenter presenter) {
    mPresenter = presenter;
  }

  @Override public void setLoadingIndicator(boolean active) {
    if (getView() == null) return;

    SwipeRefreshLayout srl = getView().findViewById(R.id.time_line_swipe_refresh_layout);
    srl.post(() -> srl.setRefreshing(active));
  }

  @Override public void showFeeds(List<Feed> feeds) {
    feedAdapter.replaceData(feeds);
    mRV.setVisibility(View.VISIBLE);
    mStateFL.setVisibility(View.GONE);
    loadDataFinish();
  }

  @Override public void showMoreFeeds(List<Feed> feeds) {
    feedAdapter.addData(feeds);
  }

  @Override public void showLoadingFeedsError() {
    mRV.setVisibility(View.GONE);
    mStateFL.setVisibility(View.GONE);
    mErrorTV.setText("LoadingFeedsError.");
    mErrorBtn.setOnClickListener(v -> {

    });
    mErrorRL.setVisibility(View.VISIBLE);
  }

  @Override public void showNoFeeds() {
    mRV.setVisibility(View.GONE);
    mStateFL.setVisibility(View.VISIBLE);
    mEmptyTV.setText("No Feeds.");
    mEmptyTV.setVisibility(View.VISIBLE);
    mErrorRL.setVisibility(View.GONE);
  }

  @Override public void showSuccessfullySaveMessage() {

  }

  @Override public void showSearch() {

  }

  @Override public void showMorePopUpMenu() {

  }
}
