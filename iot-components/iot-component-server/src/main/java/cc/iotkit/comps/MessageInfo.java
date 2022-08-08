package cc.iotkit.comps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageInfo {
    private Map<String, Object> head;
    private String type;
    private String msg;
}
