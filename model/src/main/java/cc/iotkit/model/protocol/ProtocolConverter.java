package cc.iotkit.model.protocol;

import cc.iotkit.model.Owned;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class ProtocolConverter implements Owned {

    public static final String SCRIPT_FILE_NAME = "converter.js";

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    private String desc;

    private Long createAt;
}
