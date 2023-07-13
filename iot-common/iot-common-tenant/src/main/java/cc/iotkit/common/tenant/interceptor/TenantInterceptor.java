package cc.iotkit.common.tenant.interceptor;

import cc.iotkit.common.tenant.util.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
@Slf4j
public class TenantInterceptor implements WebRequestInterceptor {

    public static final String TENANT_ID = "TENANT-ID";


    @Override
    public void preHandle(WebRequest request) {
        if (request.getHeader(TENANT_ID) != null) {
            String tenantId = request.getHeader(TENANT_ID);
            TenantContext.setTenantId(tenantId);
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {

    }
}
