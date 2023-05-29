package cc.iotkit.common.api;

import cc.iotkit.common.utils.SnowflakeIdGeneratorUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author: Longjun.Tu
 * @description:
 * @date:created in 2023/5/10 23:14
 * @modificed by:
 */
@Data
public class Request<T> extends RequestEmpty implements Serializable {

  @Valid
  @NotNull
  private T data;

  public static <T> Request<T> of(T data) {
    Request<T> request = new Request<>();
    request.setData(data);
    request.setRequestId(String.valueOf(SnowflakeIdGeneratorUtil.getInstanceSnowflake().nextId()));
    return request;
  }
}
