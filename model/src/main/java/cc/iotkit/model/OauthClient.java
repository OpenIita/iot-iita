package cc.iotkit.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * oauth2的client
 */
@Data
@Document
public class OauthClient {

    @Id
    private String clientId;

    private String name;

    private String clientSecret;

    private String allowUrl;

    private Long createAt;

}
