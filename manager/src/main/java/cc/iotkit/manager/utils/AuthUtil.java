package cc.iotkit.manager.utils;

import cc.iotkit.manager.config.Constants;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public class AuthUtil {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "";
        }
        if (authentication instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
            return token.getName();
        }
        return "";
    }

    public static List<String> getUserRoles() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream().map((role) -> role.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());
    }

    public static boolean isAdmin() {
        return AuthUtil.getUserRoles().contains(Constants.ROLE_ADMIN);
    }

}
