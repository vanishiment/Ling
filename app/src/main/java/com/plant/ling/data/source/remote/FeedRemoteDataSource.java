package com.plant.ling.data.source.remote;

import android.content.Context;
import android.util.Log;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.model.GuoKrItem;
import com.plant.ling.data.source.datasource.IFeedDataSource;
import com.plant.ling.data.source.remote.net.LingApiService;
import com.plant.ling.data.source.remote.net.LingApiServiceImpl;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

public class FeedRemoteDataSource implements IFeedDataSource {

  private static final String TAG = "FeedRemoteDataSource";

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
    return mService.getFeedList(index, limit).map(guoKr -> {
      if (guoKr != null && guoKr.result != null && guoKr.result.size() >0) {
        List<GuoKrItem> guoKrItemList = guoKr.result;
        List<Feed> feeds = new ArrayList<>();
        Feed feed;
        for (GuoKrItem guoKrItem : guoKrItemList) {
          Log.e(TAG, "getFeedList: " + guoKrItem.toString() );
          feed = new Feed();
          feed.id = guoKrItem.id;
          feed.title = guoKrItem.title;
          feed.thumbnailUrl = guoKrItem.small_image;
          feed.detailUrl = guoKrItem.url;
          feed.type = guoKrItem.subject_key;
          feed.desc = guoKrItem.summary;
          //feed.date = new Date(guoKrItem.date_published);
          feeds.add(feed);
        }
        return feeds;
      } else {
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
