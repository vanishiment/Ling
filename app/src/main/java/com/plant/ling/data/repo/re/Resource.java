package com.plant.ling.data.repo.re;

public class Resource<T> {

  public final int status;

  public final T data;

  public final String msg;

  public Resource(int status, T data, String msg) {
    this.status = status;
    this.data = data;
    this.msg = msg;
  }

  public static <T> Resource<T> success(T data) {
    return new Resource<>(Status.SUCCESS, data, null);
  }

  public static <T> Resource<T> error(T data, String msg) {
    return new Resource<>(Status.ERROR, data, msg);
  }

  public static <T> Resource<T> loading(T data) {
    return new Resource<>(Status.LOADING, data, null);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) return true;

    if (obj == null || getClass() != obj.getClass()) return false;

    Resource<?> resource = (Resource<?>) obj;
    if (status != resource.status) return false;

    if (msg != null ? !msg.equals(resource.msg) : resource.msg != null) return false;//不要简化，可读性会变差

    return data != null ? data.equals(resource.data) : resource.data == null;
  }

  @Override public int hashCode() {
    int result = status;
    result = 31 * result + (msg != null ? msg.hashCode() : 0);
    result = 31 * result + (data != null ? data.hashCode() : 0);
    return result;
  }

  @Override public String toString() {
    return "Resource{" + "status=" + status + ", data=" + data + ", msg='" + msg + '\'' + '}';
  }
}
