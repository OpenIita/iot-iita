package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokeResult {

    public static final String SUCCESS = "success";
    public static final String FAILED_UNKNOWN = "unknown";
    public static final String FAILED_OFFLINE = "offline";
    public static final String FAILED_PARAM_ERROR = "param_error";
    public static final String FAILED_NO_AUTH = "no_auth";

    private String requestId;

    private String code;
}
