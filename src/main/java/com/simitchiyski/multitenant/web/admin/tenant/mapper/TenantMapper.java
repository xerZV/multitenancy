package com.simitchiyski.multitenant.web.admin.tenant.mapper;

import com.simitchiyski.multitenant.core.abstraction.mapper.EntityToDtoMapper;
import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TenantMapper extends EntityToDtoMapper<Tenant, TenantDto> {
}
