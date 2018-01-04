package com.plant.ling.ui.detail;

import com.plant.ling.ui.base.BasePresenter;
import com.plant.ling.ui.base.BaseView;

public interface DetailContract {

  interface View extends BaseView<Presenter>{

    void setLoadingIndicator(boolean active);

    void showToolbarImg(String url);

    void showWebView(String url);

    void showWebViewError();

    void showSaveLike();

    void showSaveResult(boolean save);

  }

  interface Presenter extends BasePresenter{

    void loadDetailContent(int id);

  }
}
