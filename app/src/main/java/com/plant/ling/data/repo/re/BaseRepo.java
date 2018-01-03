package com.plant.ling.data.repo.re;

import com.plant.ling.data.source.remote.net.ApiResponse;
import io.reactivex.Flowable;

public abstract class BaseRepo<ResultType,RequestType> {

  public Flowable<ResultType> mMemory;
  public Flowable<ResultType> mDb;
  public Flowable<ResultType> mNet;

  private Flowable<ResultType> mData;

  public BaseRepo() {
    // 1.发送正在加载事件
    // 2.从数据库加载数据
    // 3.判断是否从网络读取数据，否发送数据库数据，是请求网络
    // 4.请求网络，处理数据，请求错误则发送错误事件
    // 5.保存数据库，发送新数据事件
    if (!shouldSkipLoadDb()){
      mDb = loadFromDb();
    }
    if (shouldFetch()){
      fetchFromNetwork();
    }
  }

  public abstract boolean shouldSkipLoadDb();

  public abstract Flowable<ResultType> loadFromDb();

  public abstract boolean shouldFetch();

  public abstract Flowable<ApiResponse<ResultType>> createCall();

  public abstract Flowable<ResultType> processResponse(Flowable<ApiResponse<ResultType>> response);

  public abstract boolean shouldSkipSaveDb();

  public abstract void saveCallResult(ResultType result);

  public abstract void onFetchFailed(int code,String msg);

  public Flowable<ResultType> get() {
    return loadFromDb();
  }

  private void fetchFromNetwork(){
    Flowable<ApiResponse<ResultType>> call = createCall();
    mNet = processResponse(call);
  }
}
