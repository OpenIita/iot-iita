package cc.iotkit.model.protocol;

import cc.iotkit.model.Owned;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ProtocolGateway implements Owned {

    @Id
    private String id;

    /**
     * 所属性用户id
     */
    private String uid;

    /**
     * 用户账号ID
     */
    private String uuid;

    private String name;

    private String protocol;

    private String config;

    private String script;

    private Long createAt;

}
