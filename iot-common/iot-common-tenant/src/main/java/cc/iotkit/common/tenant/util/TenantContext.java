package cc.iotkit.common.tenant.util;

public class TenantContext {
    private TenantContext() {
    }

    private static final ThreadLocal<String> tenantInfoHolder = new InheritableThreadLocal<>();

    public static void setTenantId(String tenantId) {
        tenantInfoHolder.set(tenantId);
    }

    public static String getTenantId() {
        return tenantInfoHolder.get();
    }

    public static void clear() {
        tenantInfoHolder.remove();
    }

}
