package cc.iotkit.common.tenant.util;

/**
 * 类描述...
 *
 * @author Tiger Chen
 * created on 2023/7/14 20:49
 */

public class TenantContext {
    private TenantContext() {
    }

    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static String getTenantId() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }
}
