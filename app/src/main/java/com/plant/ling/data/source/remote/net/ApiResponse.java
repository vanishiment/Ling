package com.plant.ling.data.source.remote.net;

import android.util.Log;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ApiResponse<T> {

  private static final String TAG = "ApiResponse";

  public final int code;

  public final T body;

  public final String errorMsg;

  public ApiResponse(Throwable e) {
    code = 500;
    body = null;
    errorMsg = e.getMessage();
  }

  public ApiResponse(Response<T> response) {
    code = response.code();
    if (response.isSuccessful()){
      body = response.body();
      errorMsg = null;
    }else {
      String msg = null;
      ResponseBody errorBody = response.errorBody();
      if (errorBody != null){
        try {
          msg = errorBody.string();
        } catch (IOException e) {
          Log.e(TAG,"ignore parsing error body error.");
        }
      }

      if (msg == null || msg.trim().length() == 0){
        msg = response.message();
      }
      errorMsg = msg;
      body = null;
    }
  }

  public boolean isSuccessful(){
    return code >= 200 && code < 300;
  }
}
