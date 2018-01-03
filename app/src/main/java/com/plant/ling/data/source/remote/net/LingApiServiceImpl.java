package com.plant.ling.data.source.remote.net;

import android.content.Context;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LingApiServiceImpl {

  private static LingApiService sInstance;

  public static LingApiService getInstance(Context context){
    if (sInstance == null){
      synchronized (LingApiServiceImpl.class){
        if (sInstance == null){
          sInstance = buildService(context.getApplicationContext());
        }
      }
    }
    return sInstance;
  }

  private static LingApiService buildService(Context application){
    return new Retrofit.Builder()
        .baseUrl(LingApiService.BASE_URL)
        .client(new LingOkHttpClient(application).getOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(LingApiService.class);
  }
}
