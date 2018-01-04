package com.plant.ling.ui.detail;

import com.plant.ling.data.source.repo.FeedDetailRepo;
import com.plant.ling.utils.schedulers.BaseSchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DetailPresenter implements DetailContract.Presenter {

  private FeedDetailRepo mRepo;
  private DetailContract.View mView;
  private BaseSchedulerProvider mSchedulerProvider;

  private CompositeDisposable mCompositeDisposable;

  public DetailPresenter(FeedDetailRepo repo, DetailContract.View view,
      BaseSchedulerProvider schedulerProvider) {
    this.mRepo = repo;
    this.mView = view;
    this.mSchedulerProvider = schedulerProvider;

    mCompositeDisposable = new CompositeDisposable();
    mView.setPresenter(this);
  }

  @Override public void subscribe() {

  }

  @Override public void unSubscribe() {
    mCompositeDisposable.clear();
  }

  @Override public void loadDetailContent(int id) {
    mCompositeDisposable.clear();
    Disposable disposable = mRepo.getFeedDetailById(id)
        .subscribeOn(mSchedulerProvider.io())
        .observeOn(mSchedulerProvider.ui())
        .subscribe(
            // onNext
            feedDetail -> {
          String content = feedDetail.result.content;
          mView.showWebView(content);
        },
            // onError
            throwable -> {

            });
    mCompositeDisposable.add(disposable);
  }
}
