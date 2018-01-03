package com.plant.ling.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

  public static boolean isNetConnected(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) return false;
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected();
  }

  public static boolean isNetworkConnected(Context context,int typeMobile){
    if (!isNetConnected(context)){
      return false;
    }
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager == null) return false;
    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(typeMobile);
    return null != networkInfo && networkInfo.isAvailable() && networkInfo.isConnected();
  }

  public static boolean isPhoneNetConnected(Context context){
    int typeMobile = ConnectivityManager.TYPE_MOBILE;
    return isNetworkConnected(context,typeMobile);
  }

  public static boolean isWifiNetConnected(Context context){
    int typeMobile = ConnectivityManager.TYPE_WIFI;
    return isNetworkConnected(context,typeMobile);
  }
}
