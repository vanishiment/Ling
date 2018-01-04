package com.plant.ling.data.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;
import java.util.List;

@Entity(tableName = "feed")
public class Feed {

  @PrimaryKey
  public int id;
  public String title;
  public String thumbnailUrl;
  public String desc;
  @Embedded(prefix = "media_")
  public MediaSource mediaSource;
  public String detailUrl;
  public String source;
  public String type;
  public int displayType;
  public int weight;
  public Date date;

  @Ignore
  public Feed() {
  }

  public Feed(int id, String title, String thumbnailUrl, String desc,
      MediaSource mediaSource,String detailUrl, String source, String type,
      int displayType, int weight, Date date) {
    this.id = id;
    this.title = title;
    this.thumbnailUrl = thumbnailUrl;
    this.desc = desc;
    this.mediaSource = mediaSource;
    this.detailUrl = detailUrl;
    this.source = source;
    this.type = type;
    this.displayType = displayType;
    this.weight = weight;
    this.date = date;
  }

  public static class MediaSource {

    public int type;
    public String sourceShortUrl;
    public String sourceUrl;

    @Ignore
    public MediaSource() {
    }

    public MediaSource(int type, String sourceShortUrl, String sourceUrl) {
      this.type = type;
      this.sourceShortUrl = sourceShortUrl;
      this.sourceUrl = sourceUrl;
    }
  }

  @Override public String toString() {
    return "Feed{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", thumbnailUrl='"
        + thumbnailUrl
        + '\''
        + ", desc='"
        + desc
        + '\''
        + ", mediaSource="
        + mediaSource
        + ", detailUrl='"
        + detailUrl
        + '\''
        + ", source='"
        + source
        + '\''
        + ", type='"
        + type
        + '\''
        + ", displayType="
        + displayType
        + ", weight="
        + weight
        + ", date="
        + date
        + '}';
  }
}
