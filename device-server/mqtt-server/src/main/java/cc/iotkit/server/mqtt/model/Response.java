package cc.iotkit.server.mqtt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private String id;

    private int code;

    private T data;

    public static Empty empty() {
        return new Empty();
    }

    @Data
    public static class Empty {
    }
}
