package com.plant.ling.data.source.remote;

import android.content.Context;
import com.plant.ling.data.model.FeedDetail;
import com.plant.ling.data.model.FeedDetailContent;
import com.plant.ling.data.source.datasource.IFeedDetailDataSource;
import com.plant.ling.data.source.remote.net.LingApiService;
import com.plant.ling.data.source.remote.net.LingApiServiceImpl;
import io.reactivex.Flowable;

public class FeedDetailRemoteDataSource implements IFeedDetailDataSource {

  private static FeedDetailRemoteDataSource INSTANCE;

  private LingApiService mService;

  private FeedDetailRemoteDataSource(Context context) {
    mService = LingApiServiceImpl.getInstance(context);
  }

  public static FeedDetailRemoteDataSource get(Context context) {
    if (INSTANCE == null) {
      synchronized (FeedDetailRemoteDataSource.class) {
        if (INSTANCE == null) {
          INSTANCE = new FeedDetailRemoteDataSource(context);
        }
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<FeedDetail> getFeedDetailById(int id) {
    return mService.getFeedDetail(id);
  }

  @Override public void saveFeedData(FeedDetailContent content) {
    // no need.
  }

  @Override public void deleteFeedData(FeedDetailContent content) {
    // no need.
  }
}
