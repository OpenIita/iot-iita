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

    private String mid;

    private String productKey;

    private String deviceName;

    private String type;

    private String identifier;

    private T params;

}
