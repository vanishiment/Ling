package com.plant.ling.data.source.local;

import com.plant.ling.data.model.FeedDetail;
import com.plant.ling.data.model.FeedDetailContent;
import com.plant.ling.data.source.datasource.IFeedDetailDataSource;
import io.reactivex.Flowable;

public class FeedDetailLocalDataSource implements IFeedDetailDataSource {

  private static FeedDetailLocalDataSource INSTANCE;

  public static FeedDetailLocalDataSource get(){
    if (INSTANCE == null){
      synchronized (FeedDetailLocalDataSource.class){
        if (INSTANCE == null){
          INSTANCE = new FeedDetailLocalDataSource();
        }
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<FeedDetail> getFeedDetailById(int id) {
    return null;
  }

  @Override public void saveFeedData(FeedDetailContent content) {

  }

  @Override public void deleteFeedData(FeedDetailContent content) {

  }
}
