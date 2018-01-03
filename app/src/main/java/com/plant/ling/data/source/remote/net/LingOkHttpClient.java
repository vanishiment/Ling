package com.plant.ling.data.source.remote.net;

import android.content.Context;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class LingOkHttpClient {

  private OkHttpClient okHttpClient;

  public LingOkHttpClient(Context context) {
    File cacheFile = new File(context.getApplicationContext().getCacheDir(),"plant_app_cache");
    long cacheSize = 10 * 1024 * 1024;
    Cache cache = new Cache(cacheFile,cacheSize);
    okHttpClient = new OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(new OkHttpCacheInterceptor(context))
        .addNetworkInterceptor(new OkHttpCacheInterceptor(context))
        .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20,TimeUnit.SECONDS)
        .writeTimeout(20,TimeUnit.SECONDS)
        .build();
  }

  public OkHttpClient getOkHttpClient(){
    return okHttpClient;
  }
}
