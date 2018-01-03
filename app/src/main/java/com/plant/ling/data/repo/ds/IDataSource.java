package com.plant.ling.data.repo.ds;

import android.content.Context;
import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.List;

public interface IDataSource {

  Flowable<List<Feed>> getFeedListFromDb(Context context,int id,int limit);

  Flowable<List<Feed>> getFeedListFromNet(Context context,int pageIndex);

}
