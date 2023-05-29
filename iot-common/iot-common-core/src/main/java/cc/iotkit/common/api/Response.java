package cc.iotkit.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int code;
    private String message;
    private Object data;
    private long timestamp;
}
