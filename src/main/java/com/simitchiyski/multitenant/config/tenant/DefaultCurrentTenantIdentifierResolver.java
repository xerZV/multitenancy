package com.simitchiyski.multitenant.config.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import static com.simitchiyski.multitenant.config.tenant.TenantContext.*;
import static java.util.Objects.nonNull;

public class DefaultCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    public static final String DEFAULT_TENANT = "ADMIN";

    @Override
    public String resolveCurrentTenantIdentifier() {
        final String currentTenant = getCurrentTenant();
        return nonNull(currentTenant) ? currentTenant : DEFAULT_TENANT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
