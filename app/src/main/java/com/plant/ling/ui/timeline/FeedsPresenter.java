package com.plant.ling.ui.timeline;

import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.FeedsRepo;
import com.plant.ling.utils.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;

public class FeedsPresenter implements FeedsContract.Presenter {

  private FeedsRepo mFeedsRepo;
  private FeedsContract.View mFeedView;
  private BaseSchedulerProvider mSchedulerProvider;

  private CompositeDisposable mCompositeDisposable;

  private boolean mIsFirstLoad = true;

  public FeedsPresenter(FeedsRepo feedsRepo, FeedsContract.View feedView,
      BaseSchedulerProvider schedulerProvider) {
    this.mFeedsRepo = feedsRepo;
    this.mFeedView = feedView;
    this.mSchedulerProvider = schedulerProvider;

    mCompositeDisposable = new CompositeDisposable();
    mFeedView.setPresenter(this);
  }

  @Override public void subscribe() {
    loadFeeds(false, 0, 30);
  }

  @Override public void unSubscribe() {
    mCompositeDisposable.clear();
  }

  @Override public void loadFeeds(boolean forceUpdate, int index, int limit) {
    loadFeeds(forceUpdate || mIsFirstLoad, true,false, index, limit);
    mIsFirstLoad = false;
  }

  @Override public void loadMoreFeeds(boolean forceUpdate, int index, int limit) {
    loadFeeds(forceUpdate, false,true,index, limit);
  }

  private void loadFeeds(boolean forceUpdate, boolean showLoadingUI,boolean isLoadMore, int index, int limit) {
    if (showLoadingUI) {
      mFeedView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      mFeedsRepo.refreshFeeds();
    }
    mCompositeDisposable.clear();
    Disposable disposable = mFeedsRepo.getFeedList(index, limit)
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(
            // onNext
            feeds -> {
              processFeeds(isLoadMore,feeds);
              mFeedView.setLoadingIndicator(false);
            },
            // onError
            throwable -> mFeedView.showLoadingFeedsError());
    mCompositeDisposable.add(disposable);
  }

  private void processFeeds(boolean isLoadMore,List<Feed> feeds) {
    if (feeds.isEmpty()) {
      mFeedView.showNoFeeds();
    } else {
      if (isLoadMore){
        mFeedView.showMoreFeeds(feeds);
      }else {
        mFeedView.showFeeds(feeds);
      }
    }
  }
}
