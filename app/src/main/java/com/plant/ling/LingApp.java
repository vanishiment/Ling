package com.plant.ling;

import android.app.Application;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

public class LingApp extends Application {

  @Override public void onCreate() {
    super.onCreate();
    QbSdk.initX5Environment(this,null);
    CrashReport.initCrashReport(this,"b9e177af79",BuildConfig.DEBUG);
  }
}
