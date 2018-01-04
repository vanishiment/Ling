package com.plant.ling.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.content.Context;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.datasource.IFeedDataSource;
import com.plant.ling.data.source.local.db.LingDatabase;
import io.reactivex.Flowable;
import java.util.List;

public class FeedLocalDataSource implements IFeedDataSource {

  private static FeedLocalDataSource INSTANCE;

  private final LingDatabase mDb;

  private FeedLocalDataSource(Context context) {
    mDb = LingDatabase.getInstance(context);
  }

  public static FeedLocalDataSource get(Context context){
    if (INSTANCE == null){
      synchronized (FeedLocalDataSource.class){
        INSTANCE = new FeedLocalDataSource(context);
      }
    }
    return INSTANCE;
  }

  @Override public Flowable<List<Feed>> getFeedList(int index, int limit) {
    return mDb.feedDao().getFeedList(index, limit);
  }

  @Override public Flowable<List<Feed>> getFeedListNoDbCache(int index, int limit) {
    // no need.
    return null;
  }

  @Override public Flowable<Feed> getFeedById(int id) {
    return mDb.feedDao().getFeedById(id);
  }

  @Override public void saveFeed(Feed feed) {
    mDb.feedDao().insert(feed);
  }

  @Override public void deleteFeed(Feed feed) {
    mDb.feedDao().delete(feed);
  }

  @Override public void deleteAllFeeds() {
    SupportSQLiteDatabase db = mDb.getOpenHelper().getWritableDatabase();
    db.beginTransaction();
    db.delete("feed","",null);
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  @Override public void refreshFeeds() {
    // no need.
  }
}
