package cc.iotkit.converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThingService<T> {

    public static final String TYPE_PROPERTY = "property";
    public static final String TYPE_SERVICE = "service";

    private String mid;

    private String productKey;

    private String deviceName;

    private String type;

    private String identifier;

    private T params;

}
