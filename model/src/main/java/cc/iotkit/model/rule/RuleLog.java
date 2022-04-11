package cc.iotkit.model.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "rule_log")
public class RuleLog {

    public static final String STATE_MATCHED_LISTENER = "matched_listener";
    public static final String STATE_MATCHED_FILTER = "matched_filter";
    public static final String STATE_UNMATCHED_FILTER = "unmatched_filter";
    public static final String STATE_EXECUTED_ACTION = "executed_action";

    @Id
    private String id;

    private String ruleId;

    private String state;

    private String content;

    private Boolean success;

    @Field(type = FieldType.Date)
    private Long logAt;
}
