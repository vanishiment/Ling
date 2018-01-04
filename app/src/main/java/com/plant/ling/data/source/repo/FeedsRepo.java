package com.plant.ling.data.source.repo;

import android.util.Log;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.datasource.IFeedDataSource;
import io.reactivex.Flowable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FeedsRepo implements IFeedDataSource {

  private static final String TAG = "FeedsRepo";

  private static FeedsRepo INSTANCE;

  private IFeedDataSource mLocal;
  private IFeedDataSource mRemote;

  private Map<String,Feed> mCacheFeeds;
  private boolean mCacheIsDirty = false;

  private FeedsRepo(IFeedDataSource local, IFeedDataSource remote) {
    this.mLocal = local;
    this.mRemote = remote;
  }

  public static FeedsRepo get(IFeedDataSource local, IFeedDataSource remote){
    if (INSTANCE == null){
      synchronized (FeedsRepo.class){
        if (INSTANCE == null) INSTANCE = new FeedsRepo(local, remote);
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<List<Feed>> getFeedList(int index, int limit) {
    if (mCacheFeeds != null && !mCacheIsDirty){
      return Flowable.fromIterable(mCacheFeeds.values()).toList().toFlowable();
    }else if (mCacheFeeds == null){
      mCacheFeeds = new LinkedHashMap<>();
    }

    Flowable<List<Feed>> remoteFeedList = getAndSaveRemoteFeeds(index, limit);

    if (mCacheIsDirty){
      return remoteFeedList;
    }else {
      Flowable<List<Feed>> localFeedList = getAndCacheLocalFeeds(index, limit);
      return Flowable.concat(localFeedList,remoteFeedList)
          .filter(feeds -> !feeds.isEmpty())
          .firstOrError()
          .toFlowable();
    }
  }

  @Override public Flowable<List<Feed>> getFeedListNoDbCache(int index, int limit) {
    return mRemote.getFeedList(index, limit);
  }

  @Override public Flowable<Feed> getFeedById(int id) {
    Flowable<Feed> localFeed = mLocal.getFeedById(id);
    Flowable<Feed> remoteFeed = mRemote.getFeedById(id);
    return Flowable.concat(localFeed,remoteFeed)
        .filter(feed -> feed != null)
        .firstOrError()
        .toFlowable();
  }

  @Override public void saveFeed(Feed feed) {
    mRemote.saveFeed(feed);
    mLocal.saveFeed(feed);

    if (mCacheFeeds == null){
      mCacheFeeds = new LinkedHashMap<>();
    }
    mCacheFeeds.put(String.valueOf(feed.id),feed);
  }

  @Override public void deleteFeed(Feed feed) {
    mRemote.deleteFeed(feed);
    mLocal.deleteFeed(feed);

    mCacheFeeds.remove(String.valueOf(feed.id));
  }

  @Override public void deleteAllFeeds() {
    mRemote.deleteAllFeeds();
    mLocal.deleteAllFeeds();

    if (mCacheFeeds == null) mCacheFeeds = new LinkedHashMap<>();
    mCacheFeeds.clear();
  }

  @Override public void refreshFeeds() {
    mCacheIsDirty = true;
  }

  private Flowable<List<Feed>> getAndSaveRemoteFeeds(int index,int limit){
    return mRemote.getFeedList(index, limit)
        .flatMap(feeds -> Flowable.fromIterable(feeds).doOnNext(feed -> {
          Log.e(TAG, "getAndSaveRemoteFeeds: " + feed.toString());
          mLocal.saveFeed(feed);
          mCacheFeeds.put(String.valueOf(feed.id),feed);
        }).toList().toFlowable())
        .doOnComplete(() -> mCacheIsDirty = false);
  }

  private Flowable<List<Feed>> getAndCacheLocalFeeds(int index, int limit) {
    return mLocal.getFeedList(index, limit)
        .flatMap(feeds -> Flowable.fromIterable(feeds)
            .doOnNext(feed -> mCacheFeeds.put(String.valueOf(feed.id), feed)))
        .toList()
        .toFlowable();
  }
}
