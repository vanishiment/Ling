package com.plant.ling.db;

import android.support.test.runner.AndroidJUnit4;
import com.plant.ling.data.model.Feed;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class FeedDaoTest extends DbTest {

  @Test
  public void insertAndLoad(){
    Feed feed = createFeed(1,"feed_title");
    db.feedDao().insert(feed);

    Feed loaded = db.feedDao().getAllTest().get(0);
    assertThat(loaded.title,is("feed_title"));

    Feed replacement = createFeed(2,"feed_title_replacement");
    db.feedDao().insert(replacement);
    Feed loadedReplacement = db.feedDao().getAllTest().get(1);
    assertThat(loadedReplacement.title,is("feed_title_replacement"));
  }

  private Feed createFeed(int id,String title){
    Feed feed = new Feed();
    feed.id = id;
    feed.type = String.valueOf(1);
    feed.title = title;
    feed.mediaSource = new Feed.MediaSource(1,"short_url","url");
    return feed;
  }
}
