package cc.iotkit.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * oauth2的client
 */
@Data
@Document(indexName = "oauth_client")
public class OauthClient {

    @Id
    private String clientId;

    private String name;

    private String clientSecret;

    private String allowUrl;

    @Field(type = FieldType.Date)
    private Long createAt;

}
