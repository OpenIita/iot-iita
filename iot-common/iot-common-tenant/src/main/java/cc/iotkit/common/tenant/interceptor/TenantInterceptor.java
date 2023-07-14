package cc.iotkit.common.tenant.interceptor;


import cc.iotkit.common.tenant.util.TenantContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * 类描述...
 *
 * @author Tiger Chen
 * created on 2023/7/14 20:51
 */

@Component
public class TenantInterceptor implements WebRequestInterceptor {

    public static final String TENANT_ID = "tenantId";

    @Override
    public void preHandle(WebRequest request) {
        System.out.println("TenantInterceptor: preHandle");
        if (request.getHeader(TENANT_ID) != null) {
            String tenantId = request.getHeader(TENANT_ID);
            TenantContext.setTenantId(tenantId);
        }
    }

    @Override
    public void postHandle(@NonNull WebRequest request, ModelMap model) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(@NonNull WebRequest request, Exception ex) {

    }
}
