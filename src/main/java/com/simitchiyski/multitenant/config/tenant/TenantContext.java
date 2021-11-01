package com.simitchiyski.multitenant.config.tenant;

import java.util.Optional;
import java.util.concurrent.Callable;

import static java.util.Optional.ofNullable;

public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static <T> Optional<T> withTenant(String newTenantId, Callable<T> supplier) throws Exception {
        setCurrentTenant(newTenantId);
        return ofNullable(supplier.call());
    }
}
