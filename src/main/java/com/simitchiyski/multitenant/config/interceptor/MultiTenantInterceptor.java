package com.simitchiyski.multitenant.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.simitchiyski.multitenant.config.tenant.DefaultCurrentTenantIdentifierResolver.DEFAULT_TENANT;
import static com.simitchiyski.multitenant.config.tenant.TenantContext.clear;
import static com.simitchiyski.multitenant.config.tenant.TenantContext.setCurrentTenant;
import static java.util.Objects.nonNull;

public class MultiTenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER_NAME = "X-TenantID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String tenantId = request.getHeader(TENANT_HEADER_NAME);
        setCurrentTenant(nonNull(tenantId) ? tenantId : DEFAULT_TENANT);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model) throws Exception {
        clear();
    }

}
