package cc.iotkit.model.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "task_log")
public class TaskLog {

    @Id
    private String id;

    private String taskId;

    private String content;

    private Boolean success;

    @Field(type = FieldType.Date)
    private Long logAt;
}
