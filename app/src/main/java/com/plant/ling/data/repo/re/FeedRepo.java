package com.plant.ling.data.repo.re;

import android.content.Context;
import com.plant.ling.data.source.local.db.FeedDao;
import com.plant.ling.data.source.local.db.LingDatabase;
import com.plant.ling.data.model.Feed;
import com.plant.ling.data.source.remote.net.ApiResponse;
import com.plant.ling.data.source.remote.net.LingApiService;
import com.plant.ling.data.source.remote.net.LingApiServiceImpl;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.List;

public class FeedRepo {

  private FeedDao mFeedDao;

  private LingApiService mLingApiService;

  public FeedRepo(Context context) {
    initRepo(context);
  }

  private void initRepo(Context context){
    mFeedDao = LingDatabase.getInstance(context).feedDao();
    mLingApiService = LingApiServiceImpl.getInstance(context);
  }

  public Flowable<List<Feed>> getFeedList(){
    return new BaseRepo<List<Feed>,List<Feed>>(){

      @Override public boolean shouldSkipLoadDb() {
        return false;
      }

      @Override public Flowable<List<Feed>> loadFromDb() {
        return mFeedDao.getAll();
      }

      @Override public boolean shouldFetch() {
        return true;
      }

      @Override public Flowable<ApiResponse<List<Feed>>> createCall() {
        return mLingApiService.getTestFeedList();
      }

      @Override public Flowable<List<Feed>> processResponse(Flowable<ApiResponse<List<Feed>>> response) {
        return response.doOnNext(new Consumer<ApiResponse<List<Feed>>>() {
          @Override public void accept(ApiResponse<List<Feed>> listApiResponse) throws Exception {
            if (listApiResponse.isSuccessful() && !shouldSkipSaveDb()){
                saveCallResult(listApiResponse.body);
            }else {
              onFetchFailed(listApiResponse.code,listApiResponse.errorMsg);
            }
          }
        }).map(new Function<ApiResponse<List<Feed>>, List<Feed>>() {
          @Override public List<Feed> apply(ApiResponse<List<Feed>> listApiResponse)
              throws Exception {
            return listApiResponse.body;
          }
        });
      }

      @Override public boolean shouldSkipSaveDb() {
        return false;
      }

      @Override public void saveCallResult(List<Feed> result) {
        for (Feed f : result) {
          mFeedDao.insert(f);
        }
      }

      @Override public void onFetchFailed(int code,String msg) {

      }
    }.get();
  }
}
