package cc.iotkit.data;

import cc.iotkit.model.OauthClient;

public interface IOauthClientData extends ICommonData<OauthClient, String> {

    OauthClient findByClientId(String clientId);

}
