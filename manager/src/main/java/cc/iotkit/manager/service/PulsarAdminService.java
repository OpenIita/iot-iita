package cc.iotkit.manager.service;

import com.google.common.collect.Sets;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PulsarAdminService {

    @Value("${pulsar.service}")
    private String pulsarServiceUrl;

    private PulsarAdmin pulsarAdmin;

    private PulsarAdmin getPulsarAdmin() throws PulsarClientException {
        if (pulsarAdmin == null) {
            pulsarAdmin = PulsarAdmin.builder()
                    .serviceHttpUrl(pulsarServiceUrl)
                    .build();
        }
        return pulsarAdmin;
    }

    public boolean tenantExists(String uid) throws PulsarAdminException, PulsarClientException {
        PulsarAdmin pulsarAdmin = getPulsarAdmin();
        return pulsarAdmin.tenants().getTenants().contains(uid);
    }

    public void createTenant(String uid) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin pulsarAdmin = getPulsarAdmin();
        pulsarAdmin.tenants().createTenant(uid, TenantInfo.builder()
                .adminRoles(new HashSet<>())
                .allowedClusters(Sets.newHashSet("standalone"))
                .build());

        pulsarAdmin.namespaces().createNamespace(uid + "/default");
    }

    public void deleteTenant(String uid) throws PulsarClientException, PulsarAdminException {
        PulsarAdmin pulsarAdmin = getPulsarAdmin();
        pulsarAdmin.tenants().deleteTenant(uid);
    }

}
