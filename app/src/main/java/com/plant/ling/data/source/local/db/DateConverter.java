package com.plant.ling.data.source.local.db;

import android.arch.persistence.room.TypeConverter;
import java.util.Date;

public class DateConverter {

  @TypeConverter
  public static Date toDate(Long timestamp){
    return timestamp == null ? null : new Date(timestamp);
  }

  @TypeConverter
  public static Long toTimeStamp(Date date){
    return date == null ? null : date.getTime();
  }
}
