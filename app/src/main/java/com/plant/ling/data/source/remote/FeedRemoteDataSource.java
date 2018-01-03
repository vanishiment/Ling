package com.plant.ling.data.source.remote;

import android.content.Context;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.FeedDataSource;
import com.plant.ling.data.source.remote.net.ApiResponse;
import com.plant.ling.data.source.remote.net.LingApiService;
import com.plant.ling.data.source.remote.net.LingApiServiceImpl;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

public class FeedRemoteDataSource implements FeedDataSource{

  private static FeedRemoteDataSource INSTANCE;

  private LingApiService mService;

  private FeedRemoteDataSource(Context context){
    mService = LingApiServiceImpl.getInstance(context);
  }

  public static FeedRemoteDataSource get(Context context){
    if (INSTANCE == null){
      synchronized (FeedRemoteDataSource.class){
        INSTANCE = new FeedRemoteDataSource(context);
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<List<Feed>> getFeedList(int index, int limit) {
    return mService.getFeedList(index, limit).map(new Function<ApiResponse<List<Feed>>, List<Feed>>() {
      @Override public List<Feed> apply(ApiResponse<List<Feed>> listApiResponse) throws Exception {
        if (listApiResponse.isSuccessful()){
          return listApiResponse.body;
        }
        return null;
      }
    });
  }

  @Override public Flowable<List<Feed>> getFeedListNoDbCache(int index, int limit) {
    // no need.
    return null;
  }

  @Override public Flowable<Feed> getFeedById(int id) {
    // no need.
    return null;
  }

  @Override public void saveFeed(Feed feed) {
    // no need.
  }

  @Override public void deleteFeed(Feed feed) {
    // no need.
  }

  @Override public void deleteAllFeeds() {
    // no need.
  }

  @Override public void refreshFeeds() {
    // no need.
  }
}
