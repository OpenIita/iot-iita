package cc.iotkit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvokeResult {

    private String requestId;

    private long time;

    public InvokeResult(String requestId) {
        this.requestId = requestId;
        this.time = System.currentTimeMillis();
    }
}
