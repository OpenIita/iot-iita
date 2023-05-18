package cc.iotkit.common.api;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:12
 * @modificed by:
 */
@Data
public class RequestEmpty implements Serializable {

  private String requestId;

  public static RequestEmpty of() {
    RequestEmpty request = new RequestEmpty();
    request.setRequestId(UUID.randomUUID().toString());
    return request;
  }

}
