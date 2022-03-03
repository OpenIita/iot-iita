package cc.iotkit.model.rule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class TaskLog {

    @Id
    private String id;

    private String taskId;

    private String content;

    private Boolean success;

    private Long logAt;
}
