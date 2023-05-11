package cc.iotkit.common.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:14
 * @modificed by:
 */
public class Request<T> extends RequestEmpty implements Serializable {
  private  T data;

  public Request() {
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> Request<T> of(T data) {
    Request<T> request = new Request();
    request.setData(data);
    request.setRequestId(UUID.randomUUID().toString());
    return request;
  }
}
