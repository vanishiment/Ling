package com.plant.ling.ui.timeline;

import com.plant.ling.data.model.Feed;
import com.plant.ling.ui.base.BasePresenter;
import com.plant.ling.ui.base.BaseView;
import java.util.List;

public interface FeedsContract {

  interface View extends BaseView<Presenter>{

    void setLoadingIndicator(boolean active);

    void showFeeds(List<Feed> feeds);

    void showMoreFeeds(List<Feed> feeds);

    void showLoadingFeedsError();

    void showNoFeeds();

    void showSuccessfullySaveMessage();

    void showSearch();

    void showMorePopUpMenu();
  }

  interface Presenter extends BasePresenter{

    void loadFeeds(boolean forceUpdate,int index,int limit);

    void loadMoreFeeds(boolean forceUpdate,int index,int limit);
  }
}
