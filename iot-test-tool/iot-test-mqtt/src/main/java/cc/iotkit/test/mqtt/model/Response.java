package cc.iotkit.test.mqtt.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String id;

    private int code;

    private Map<String, Object> data;
}