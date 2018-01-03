package com.plant.ling.data.source.local.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.plant.ling.data.model.Feed;
import io.reactivex.Flowable;
import java.util.List;

@Dao
public interface FeedDao {

  @Query("select * from feed")
  Flowable<List<Feed>> getAll();

  @Query("select * from feed")
  List<Feed> getAllTest();

  @Query("select * from feed where id > :id limit :limit")
  Flowable<List<Feed>> getFeedList(int id,int limit);

  @Query("select * from feed where id = :id")
  Flowable<Feed> getFeedById(int id);

  @Insert
  void insert(Feed feed);

  @Delete
  void delete(Feed feed);
}
