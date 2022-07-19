package cc.iotkit.data.dao;

import cc.iotkit.data.model.TbOauthClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthClientRepository extends JpaRepository<TbOauthClient, String> {

    TbOauthClient findByClientId(String clientId);

}
