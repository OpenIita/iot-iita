package cc.iotkit.manager.service;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.JsonUtil;
import cc.iotkit.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class KeycloakAdminService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak-admin-user}")
    private String adminUser;

    @Value("${keycloak-admin-password}")
    private String adminPassword;

    @Value("${keycloak-admin-clientid}")
    private String adminClientId;

    private Keycloak keycloak;

    private Keycloak getKeycloak() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(authServerUrl)
                    .username(adminUser)
                    .password(adminPassword)
                    .clientId(adminClientId)
                    .realm(realm)
                    .build();
        }
        return keycloak;
    }

    public String createUser(UserInfo user, String pwd) {
        Keycloak keycloak = getKeycloak();
        UsersResource usersResource = keycloak.realm(realm)
                .users();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUid());
        userRepresentation.setGroups(Collections.singletonList(getGroup(user.getType())));
        userRepresentation.setRealmRoles(user.getRoles());
        if (user.getEmail() != null) {
            userRepresentation.setEmail(user.getEmail());
        }
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(user.getNickName());

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(pwd);
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        javax.ws.rs.core.Response response = usersResource.create(userRepresentation);
        String url = response.getLocation().getPath();
        String newUid = url.substring(url.lastIndexOf("/") + 1);

        if (response.getStatus() >= 300) {
            log.error("create userRepresentation response:{}", JsonUtil.toJsonString(response));
            throw new BizException("create keycloak user failed");
        }

        return newUid;
    }

    public void updateUser(UserInfo user) {
        Keycloak keycloak = getKeycloak();
        UserResource userResource = keycloak.realm(realm)
                .users().get(user.getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (user.getUid() != null) {
            userRepresentation.setUsername(user.getUid());
        }
        if (user.getEmail() != null) {
            userRepresentation.setEmail(user.getEmail());
        }
        if (user.getType() != null) {
            userRepresentation.setGroups(Arrays.asList(getGroup(user.getType())));
        }
        if (user.getRoles() != null) {
            userRepresentation.setRealmRoles(user.getRoles());
        }
        userResource.update(userRepresentation);
    }

    public UserInfo getUser(String uid) {
        Keycloak keycloak = getKeycloak();
        List<UserRepresentation> users = keycloak.realm(realm)
                .users().search(uid);
        if (users.size() == 0) {
            return null;
        }
        UserRepresentation user = users.get(0);

        return UserInfo.builder()
                .id(user.getId())
                .uid(uid)
                .build();
    }

    public void resetUserPwd(String id, String pwd) {
        Keycloak keycloak = getKeycloak();
        UserResource userResource = keycloak.realm(realm)
                .users().get(id);
        UserRepresentation userRepresentation = userResource.toRepresentation();

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(pwd);
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Arrays.asList(credentialRepresentation));

        userResource.update(userRepresentation);
    }

    public void deleteUser(String id) {
        Keycloak keycloak = getKeycloak();
        UserResource userResource = keycloak.realm(realm)
                .users().get(id);
        try {
            userResource.remove();
        } catch (javax.ws.rs.NotFoundException e) {
            log.warn("user does not exist");
        }
    }

    private String getGroup(Integer type) {
        if (type == null) {
            return "";
        }
        return type == UserInfo.USER_TYPE_PLATFORM
                ? "platform" : "client";
    }

}
