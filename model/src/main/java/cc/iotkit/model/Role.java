package cc.iotkit.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Role {

    private String id;

    private String name;

}
