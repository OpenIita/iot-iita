package cc.iotkit.model.protocol;

import cc.iotkit.model.Owned;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ProtocolComponent implements Owned {

    public static final String STATE_STOPPED = "stopped";
    public static final String STATE_RUNNING = "running";

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    private String name;

    private String protocol;

    private String jarFile;

    private String config;

    private String scriptFile;

    private String state;

    private Long createAt;

}
