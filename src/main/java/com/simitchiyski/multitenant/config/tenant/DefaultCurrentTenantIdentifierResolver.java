package com.simitchiyski.multitenant.config.tenant;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import static com.simitchiyski.multitenant.config.tenant.TenantContext.getCurrentTenant;
import static java.util.Objects.nonNull;

public class DefaultCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    public static final String DEFAULT = "ADMIN"; // TODO

    @Override
    public String resolveCurrentTenantIdentifier() {
        final String currentTenant = getCurrentTenant();
        return nonNull(currentTenant) ? currentTenant : DEFAULT;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

}
