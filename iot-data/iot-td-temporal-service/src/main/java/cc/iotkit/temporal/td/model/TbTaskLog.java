package cc.iotkit.temporal.td.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbTaskLog {

    private Long time;

    private String taskId;

    private String content;

    private Boolean success;

}
