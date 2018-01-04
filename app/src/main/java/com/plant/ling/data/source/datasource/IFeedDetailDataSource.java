package com.plant.ling.data.source.datasource;

import com.plant.ling.data.model.FeedDetail;
import com.plant.ling.data.model.FeedDetailContent;
import io.reactivex.Flowable;

public interface IFeedDetailDataSource {

  Flowable<FeedDetail> getFeedDetailById(int id);

  void saveFeedData(FeedDetailContent content);

  void deleteFeedData(FeedDetailContent content);
}
