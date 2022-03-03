package cc.iotkit.tppa.xiaodu.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {

    private String namespace;
    private String name;
    private String messageId;
    private String payloadVersion;

}
