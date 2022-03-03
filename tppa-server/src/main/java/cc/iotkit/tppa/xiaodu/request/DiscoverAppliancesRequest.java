package cc.iotkit.tppa.xiaodu.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class DiscoverAppliancesRequest {

    private Header header;

    private Payload payload;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Payload extends PayloadToken {
        private String openUid;
    }

}
