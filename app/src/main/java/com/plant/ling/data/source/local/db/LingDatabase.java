package com.plant.ling.data.source.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import com.plant.ling.data.model.Feed;

@Database(entities = {Feed.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class LingDatabase extends RoomDatabase {

  private static final String DB_NAME = "ling_app.db";
  private static LingDatabase sInstance;

  public abstract FeedDao feedDao();

  public static LingDatabase getInstance(Context context){
    if (sInstance == null){
      synchronized (LingDatabase.class){
        if (sInstance == null){
          sInstance = Room.databaseBuilder(context.getApplicationContext(),LingDatabase.class,DB_NAME).build();
        }
      }
    }
    return sInstance;
  }
}
