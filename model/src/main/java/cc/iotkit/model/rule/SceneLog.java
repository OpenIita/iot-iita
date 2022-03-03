package cc.iotkit.model.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class SceneLog {

    public static final String STATE_MATCHED_LISTENER = "matched_listener";
    public static final String STATE_MATCHED_FILTER = "matched_filter";
    public static final String STATE_UNMATCHED_FILTER = "unmatched_filter";
    public static final String STATE_EXECUTED_ACTION = "executed_action";

    @Id
    private String id;

    private String sceneId;

    private String state;

    private String content;

    private Boolean success;

    private Long logAt;
}
