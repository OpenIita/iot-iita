package cc.iotkit.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oauth_client")
public class TbOauthClient {

    @Id
    private String id;

    private String clientId;

    private String name;

    private String clientSecret;

    private String allowUrl;

    private Long createAt;

}
