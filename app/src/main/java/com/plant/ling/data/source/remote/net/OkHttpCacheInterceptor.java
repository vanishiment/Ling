package com.plant.ling.data.source.remote.net;

import android.content.Context;
import android.support.annotation.NonNull;
import com.plant.ling.utils.NetworkUtil;
import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpCacheInterceptor implements Interceptor {

  public Context context;

  public OkHttpCacheInterceptor(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override public Response intercept(@NonNull Chain chain) throws IOException {
    Request request = chain.request();
    if (NetworkUtil.isNetConnected(context)){
      Response response = chain.proceed(request);
      return response.newBuilder()
          .removeHeader("Pragma")
          .removeHeader("Cache-Control")
          .header("Cache-Control", "public, max-age=" + 60)
          .build();
    }else {
      Request newReq = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
      Response response = chain.proceed(newReq);
      long maxStale = 60 * 60 * 24 * 3;
      return response.newBuilder()
          .removeHeader("Pragma")
          .removeHeader("Cache-Control")
          .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
          .build();
    }
  }
}
