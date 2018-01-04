package com.plant.ling.data.source.datasource;

import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.List;

public interface IFeedDataSource {

  Flowable<List<Feed>> getFeedList(int index,int limit);

  Flowable<List<Feed>> getFeedListNoDbCache(int index,int limit);

  Flowable<Feed> getFeedById(int id);

  void saveFeed(Feed feed);

  void deleteFeed(Feed feed);

  void deleteAllFeeds();

  void refreshFeeds();

}
