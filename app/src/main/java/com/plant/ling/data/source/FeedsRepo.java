package com.plant.ling.data.source;

import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FeedsRepo implements FeedDataSource {

  private static FeedsRepo INSTANCE;

  private FeedDataSource mLocal;
  private FeedDataSource mRemote;

  private Map<String,Feed> mCacheFeeds;
  private boolean mCacheIsDirty = false;

  private FeedsRepo(FeedDataSource local, FeedDataSource remote) {
    this.mLocal = local;
    this.mRemote = remote;
  }

  public static FeedsRepo get(FeedDataSource local, FeedDataSource remote){
    if (INSTANCE == null){
      synchronized (FeedsRepo.class){
        INSTANCE = new FeedsRepo(local, remote);
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
