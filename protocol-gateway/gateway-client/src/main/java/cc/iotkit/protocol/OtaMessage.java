package cc.iotkit.protocol;

import lombok.Data;

/**
 * OTA消息
 */
@Data
public class OtaMessage {

    private final String TYPE_PROGRESS="progress";
    private final String TYPE_RESULT="result";

    private final String PROGRESS_DOWNLOADING="downloading";
    private final String PROGRESS_DOWNLOADED="downloaded";
    private final String PROGRESS_UPGRADING="upgrading";
    private final String PROGRESS_UPGRADED="upgraded";

    private String productKey;

    private String deviceName;

    private String type;

    private String jobId;

    private String progress;

    private String result;
}
