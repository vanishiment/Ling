package com.plant.ling.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.plant.ling.R;
import com.plant.ling.data.source.repo.FeedsRepo;
import com.plant.ling.data.source.local.FeedLocalDataSource;
import com.plant.ling.data.source.remote.FeedRemoteDataSource;
import com.plant.ling.ui.search.SearchActivity;
import com.plant.ling.ui.like.LikeFragment;
import com.plant.ling.ui.mine.MineFragment;
import com.plant.ling.ui.timeline.FeedsPresenter;
import com.plant.ling.ui.timeline.TimeLineFragment;
import com.plant.ling.utils.schedulers.SchedulerProvider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final int REQUEST_CODE_SEARCH = 1000;

  @BindView(R.id.main_view_pager) ViewPager mVP;
  @BindView(R.id.main_nav_view) BottomNavigationView mNV;
  @BindView(R.id.main_toolbar) Toolbar mToolbar;
  @BindView(R.id.main_fab) FloatingActionButton mFAB;

  List<Fragment> mFragmentList;
  MainViewPagerAdapter mVPAdapter;

  private TimeLineFragment mTimeLine;
  private LikeFragment mLike;
  private MineFragment mMine;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);
    initFragments();
    initView(savedInstanceState);
  }

  private void initFragments() {
    if (mFragmentList == null) {
      mFragmentList = new ArrayList<>();
    }
    if (mTimeLine == null){
      mTimeLine = TimeLineFragment.newInstance();
      FeedsPresenter presenter = new FeedsPresenter(
          FeedsRepo.get(FeedLocalDataSource.get(getApplicationContext()),FeedRemoteDataSource.get(getApplicationContext())),
          mTimeLine,
          SchedulerProvider.get());
    }
    if (mLike == null){
      mLike = LikeFragment.newInstance();
    }
    if (mMine == null){
      mMine = MineFragment.newInstance();
    }
    mFragmentList.clear();
    mFragmentList.add(mTimeLine);
    mFragmentList.add(mLike);
    mFragmentList.add(mMine);
  }

  private void initView(Bundle savedInstanceState) {
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null){
      actionBar.setTitle(R.string.app_name);
    }
    animateToolbar();
        mFAB.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    mVPAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
    mVP.setOffscreenPageLimit(2);
    mVP.addOnPageChangeListener(mOnPageChangeListener);
    mVP.setAdapter(mVPAdapter);

    mNV.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mVP.removeOnPageChangeListener(mOnPageChangeListener);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    switch (id){
      case R.id.action_search:
        View menuSearch = mToolbar.findViewById(R.id.action_search);
        Bundle option = ActivityOptionsCompat.makeSceneTransitionAnimation(this,menuSearch,getString(R.string.transition_search_back)).toBundle();
        ((Activity) this).startActivityForResult(new Intent(this,SearchActivity.class),REQUEST_CODE_SEARCH,option);
        return true;
      case R.id.action_filter:
        return true;
      case R.id.action_scan:
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode){
      case REQUEST_CODE_SEARCH:
        View menuSearch = mToolbar.findViewById(R.id.action_search);
        if (menuSearch != null){
          menuSearch.setAlpha(1F);
        }
        if (resultCode == SearchActivity.RESULT_CODE_SAVE){

        }
        break;
    }
  }

  private void animateToolbar() {
    // this is gross but toolbar doesn't expose it's children to animate them :(
    View t = mToolbar.getChildAt(0);
    if (t != null && t instanceof TextView) {
      TextView title = (TextView) t;

      // fade in and space out the title.  Animating the letterSpacing performs horribly so
      // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
      title.setAlpha(0f);
      title.setScaleX(0.8f);

      title.animate()
          .alpha(1f)
          .scaleX(1f)
          .setStartDelay(300)
          .setDuration(900)
          .setInterpolator(new FastOutSlowInInterpolator());
    }
  }

  private ViewPager.OnPageChangeListener mOnPageChangeListener =
      new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override public void onPageSelected(int position) {
          switch (position) {
            case 0:
              mNV.setSelectedItemId(R.id.menu_nav_action_time_line);
              break;
            case 1:
              mNV.setSelectedItemId(R.id.menu_nav_action_like);
              break;
            case 2:
              mNV.setSelectedItemId(R.id.menu_nav_action_mine);
              break;
            default:
              break;
          }
        }

        @Override public void onPageScrollStateChanged(int state) {

        }
      };

  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            case R.id.menu_nav_action_time_line:
              mVP.setCurrentItem(0,false);
              return true;
            case R.id.menu_nav_action_like:
              mVP.setCurrentItem(1,false);
              return true;
            case R.id.menu_nav_action_mine:
              mVP.setCurrentItem(2,false);
              return true;
            default:
              return false;
          }
        }
      };

  private class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    MainViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
      super(fm);
      this.fragmentList = fragmentList;
    }

    @Override public Fragment getItem(int position) {
      return fragmentList.get(position);
    }

    @Override public int getCount() {
      return fragmentList == null ? 0 : fragmentList.size();
    }
  }
}
