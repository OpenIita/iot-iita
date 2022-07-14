package cc.iotkit.data.service;

import cc.iotkit.data.IOauthClientData;
import cc.iotkit.model.OauthClient;
import cc.iotkit.model.Paging;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OauthClientDataImpl implements IOauthClientData {
    @Override
    public OauthClient findByClientId(String clientId) {
        return null;
    }

    @Override
    public OauthClient findById(String s) {
        return null;
    }

    @Override
    public OauthClient save(OauthClient data) {
        return null;
    }

    @Override
    public OauthClient add(OauthClient data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<OauthClient> findAll() {
        return null;
    }

    @Override
    public Paging<OauthClient> findAll(int page, int size) {
        return null;
    }
}
