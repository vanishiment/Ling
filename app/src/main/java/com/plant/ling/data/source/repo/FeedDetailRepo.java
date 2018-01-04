package com.plant.ling.data.source.repo;

import com.plant.ling.data.model.FeedDetail;
import com.plant.ling.data.model.FeedDetailContent;
import com.plant.ling.data.source.datasource.IFeedDetailDataSource;
import io.reactivex.Flowable;

public class FeedDetailRepo implements IFeedDetailDataSource {

  private static FeedDetailRepo INSTANCE;

  private IFeedDetailDataSource mLocal;
  private IFeedDetailDataSource mRemote;

  private FeedDetailRepo(IFeedDetailDataSource local, IFeedDetailDataSource remote) {
    this.mLocal = local;
    this.mRemote = remote;
  }

  public static FeedDetailRepo get(IFeedDetailDataSource local, IFeedDetailDataSource remote){
    if (INSTANCE == null){
      synchronized (FeedDetailRepo.class){
        if (INSTANCE == null) INSTANCE = new FeedDetailRepo(local, remote);
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<FeedDetail> getFeedDetailById(int id) {
    return mRemote.getFeedDetailById(id);
  }

  @Override public void saveFeedData(FeedDetailContent content) {

  }

  @Override public void deleteFeedData(FeedDetailContent content) {

  }
}
