package cc.iotkit.common.api;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:12
 * @modificed by:
 */
public class RequestEmpty implements Serializable {


  private String language;


  @Deprecated
  private String requestId;

  public RequestEmpty() {
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getRequestId() {
    return this.requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public static RequestEmpty of() {
    RequestEmpty request = new RequestEmpty();
    request.setRequestId(UUID.randomUUID().toString());
    return request;
  }

}
