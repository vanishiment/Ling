package com.plant.ling.data.source.remote.net;

import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LingApiService {

  String BASE_URL = "http://apis.guokr.com/minisite/";

  @GET("article.json?retrieve_type=by_minisite")
  Flowable<ApiResponse<List<Feed>>> getFeedList(@Query("offset")int offset, @Query("limit")int limit);

}
