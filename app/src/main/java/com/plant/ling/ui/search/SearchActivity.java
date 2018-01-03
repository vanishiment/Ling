package com.plant.ling.ui.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.plant.ling.R;

public class SearchActivity extends AppCompatActivity {

  public static final int RESULT_CODE_SAVE = 7;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    ButterKnife.bind(this);
  }
}
