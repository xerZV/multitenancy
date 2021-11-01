package com.simitchiyski.multitenant.core.admin.tenant;

import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface TenantService {
    @NotNull List<Tenant> findAll();
    @NotNull Tenant create(final @NotNull @Valid TenantCreateDto dto);
}
