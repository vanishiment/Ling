package com.plant.ling.ui.detail;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.plant.ling.R;
import com.plant.ling.data.source.local.FeedDetailLocalDataSource;
import com.plant.ling.data.source.remote.FeedDetailRemoteDataSource;
import com.plant.ling.data.source.repo.FeedDetailRepo;
import com.plant.ling.utils.schedulers.SchedulerProvider;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class DetailActivity extends AppCompatActivity implements DetailContract.View{

  public static final String DETAIL_WEB_URL_ID = "DETAIL_WEB_URL_ID";
  public static final String DETAIL_IMG_URL = "DETAIL_IMG_URL";
  public static final String DETAIL_TITLE = "DETAIL_TITLE";

  @BindView(R.id.detail_toolbar) Toolbar mToolbar;
  @BindView(R.id.detail_toolbar_iv) ImageView mToolbarIV;
  @BindView(R.id.detail_fab) FloatingActionButton mFAB;
  @BindView(R.id.content_detail_web_view) WebView mWebView;
  @BindView(R.id.content_detail_progress_bar) ProgressBar mPB;

  private DetailPresenter mPresenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    getWindow().setFormat(PixelFormat.TRANSLUCENT);
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
    setContentView(R.layout.activity_detail);
    ButterKnife.bind(this);
    mPresenter = new DetailPresenter(FeedDetailRepo.get(FeedDetailLocalDataSource.get(),
        FeedDetailRemoteDataSource.get(getApplicationContext())),this, SchedulerProvider.get());
    mPresenter.subscribe();
    setupView();
    handleIntent(getIntent());
  }

  private void setupView(){
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null){
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeButtonEnabled(true);
      //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
    }
    mFAB.setOnClickListener(v -> {
      Snackbar.make(v, "Save this successfully!", Snackbar.LENGTH_LONG)
          .setAction("Undo", v1 -> {

          })
          .show();
    });
    WebSettings webSettings = mWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setBuiltInZoomControls(false);
    webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    mWebView.setWebViewClient(new WebViewClient(){
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }
    });
    mWebView.setWebChromeClient(new WebChromeClient(){
      @Override public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 0) mPB.setVisibility(View.VISIBLE);
        mPB.setProgress(newProgress);
        if (newProgress == 100) mPB.setVisibility(View.GONE);
      }
    });
    mWebView.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        return false;
      }
    });

    mPB.setMax(100);
    mPB.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_drawable));
  }

  private void handleIntent(Intent intent) {
    int id = intent.getIntExtra(DETAIL_WEB_URL_ID,-1);
    String imgUrl = intent.getStringExtra(DETAIL_IMG_URL);
    String title = intent.getStringExtra(DETAIL_TITLE);
    if (!TextUtils.isEmpty(title) && getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    showToolbarImg(imgUrl);
    mPresenter.loadDetailContent(id);
  }

  @Override protected void onPause() {
    super.onPause();
    mPresenter.unSubscribe();
  }

  @Override public void setPresenter(DetailContract.Presenter presenter) {
  }

  @Override public void setLoadingIndicator(boolean active) {

  }

  @Override public void showToolbarImg(String url) {
    RequestOptions options = RequestOptions.centerCropTransform().error(R.drawable.background).placeholder(R.drawable.background).diskCacheStrategy(
        DiskCacheStrategy.AUTOMATIC);
    Glide.with(this).load(url).apply(options).into(mToolbarIV);
  }

  @Override public void showWebView(String url) {
    if (TextUtils.isEmpty(url)) showWebViewError();
    showWebContent(url);
  }

  @Override public void showWebViewError() {

  }

  @Override public void showSaveLike() {

  }

  @Override public void showSaveResult(boolean save) {

  }

  public void showWebContent(String content) {

    String css;

    if (false) {
      css = "<div class=\"article\" id=\"contentMain\" style=\"background-color:#212b30\">";
    } else {
      css = "<div class=\"article\" id=\"contentMain\">";
    }

    String result = "<!DOCTYPE html>\n"
        + "<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
        + "<head>\n<meta charset=\"utf-8\" />\n"
        + "\n<link rel=\"stylesheet\" href=\"file:///android_asset/guokr_master.css\" />\n"
        + css
        + "<script src=\"file:///android_asset/guokr.base.js\"></script>\n"
        + "<script src=\"file:///android_asset/guokr.articleInline.js\"></script>"
        + "<script>\n"
        + "var ukey = null;\n"
        + "</script>\n"
        + "\n</head>\n<div class=\"content\" id=\"articleContent\"><body>\n"
        + content
        + "\n</div></body>\n</html>";
    mWebView.loadDataWithBaseURL("x-data://base", result,"text/html","utf-8",null);
  }

}
