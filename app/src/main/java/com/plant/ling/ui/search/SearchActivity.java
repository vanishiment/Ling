package com.plant.ling.ui.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import butterknife.ButterKnife;
import com.plant.ling.R;

public class SearchActivity extends AppCompatActivity {

  public static final int RESULT_CODE_SAVE = 7;

  //@BindView(R.id.search_tool_bar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
    //setSupportActionBar(mToolbar);
    //ActionBar actionBar = getSupportActionBar();
    //if (actionBar != null){
    //  actionBar.setDisplayHomeAsUpEnabled(true);
    //}
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    //getMenuInflater().inflate(R.menu.menu_search,menu);
    //MenuItem searchItem = menu.findItem(R.id.detail_action_search);
    //SearchView searchView = (SearchView) searchItem.getActionView();
    //searchView.setIconified(false);
    //searchView.setSubmitButtonEnabled(true);
    //searchView.setQueryHint("搜索故事");
    return true;
  }
}
