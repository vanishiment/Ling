package com.plant.ling.data.repo.ds;

import android.content.Context;
import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.List;

public class DataSourceManager {

  IDataSource iDataSource;

  public DataSourceManager(IDataSource iDataSource) {
    this.iDataSource = iDataSource;
  }

  public Flowable<List<Feed>> getFeedList(Context context,int id,int limit,int pageIndex){
    Flowable<List<Feed>> feeds = iDataSource.getFeedListFromDb(context, id, limit);
    if (feeds == null){
      feeds = iDataSource.getFeedListFromNet(context, pageIndex);
    }
    return feeds;
  }
}
