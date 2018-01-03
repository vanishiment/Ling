package com.plant.ling.db;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import com.plant.ling.data.source.local.db.LingDatabase;
import org.junit.After;
import org.junit.Before;

public abstract class DbTest {

  protected LingDatabase db;

  @Before
  public void initDb(){
    db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),LingDatabase.class).build();
  }

  @After
  public void closeDb(){
    db.close();
  }
}
