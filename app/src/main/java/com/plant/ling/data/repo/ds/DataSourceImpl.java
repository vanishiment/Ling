package com.plant.ling.data.repo.ds;

import android.content.Context;
import com.plant.ling.data.source.local.db.LingDatabase;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.remote.net.ApiResponse;
import com.plant.ling.data.source.remote.net.LingApiServiceImpl;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.List;

public class DataSourceImpl implements IDataSource {

  @Override public Flowable<List<Feed>> getFeedListFromDb(Context context,int id, int limit) {
    return LingDatabase.getInstance(context).feedDao().getFeedList(id, limit);
  }

  @Override public Flowable<List<Feed>> getFeedListFromNet(Context context,int pageIndex) {
    return LingApiServiceImpl.getInstance(context).getTestFeedList().map(new Function<ApiResponse<List<Feed>>, List<Feed>>() {
      @Override public List<Feed> apply(ApiResponse<List<Feed>> listApiResponse) throws Exception {
        if (listApiResponse.isSuccessful()){
          return listApiResponse.body;
        }
        return null;
      }
    });
  }
}
