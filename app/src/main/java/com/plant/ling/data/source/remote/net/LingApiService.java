package com.plant.ling.data.source.remote.net;

import com.plant.ling.data.model.FeedDetail;
import com.plant.ling.data.model.GuoKr;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LingApiService {

  String BASE_URL = "http://apis.guokr.com/minisite/";

  @GET("article.json?retrieve_type=by_minisite")
  Flowable<GuoKr> getFeedList(@Query("offset")int offset, @Query("limit")int limit);

  @GET("article/{id}.json")
  Flowable<FeedDetail> getFeedDetail(@Path("id")int id);
}
