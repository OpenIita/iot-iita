package cc.iotkit.manager.utils;

import cc.iotkit.common.Constants;
import cn.dev33.satoken.stp.StpUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthUtil {

    public static String getUserId() {
        return StpUtil.getLoginId().toString();
    }

    public static List<String> getUserRoles() {
        return StpUtil.getRoleList();
    }

    public static boolean isAdmin() {
        return AuthUtil.getUserRoles().contains(Constants.ROLE_ADMIN);
    }

    public static boolean isClientUser() {
        return AuthUtil.getUserRoles().contains(Constants.ROLE_CLIENT);
    }

    public static boolean hasWriteRole() {
        return AuthUtil.getUserRoles().contains(Constants.ROLE_WRITE);
    }

}
