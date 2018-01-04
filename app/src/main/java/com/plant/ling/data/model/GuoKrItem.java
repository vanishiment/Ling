package com.plant.ling.data.model;

public class GuoKrItem {

  public int id;
  public String title;
  public String small_image;
  public String date_published;
  public String url;
  public String subject_key;
  public String summary;

  @Override public String toString() {
    return "GuoKrItem{"
        + "id="
        + id
        + ", title='"
        + title
        + '\''
        + ", small_image='"
        + small_image
        + '\''
        + ", date_published='"
        + date_published
        + '\''
        + ", url='"
        + url
        + '\''
        + ", subject_key='"
        + subject_key
        + '\''
        + ", summary='"
        + summary
        + '\''
        + '}';
  }
}
