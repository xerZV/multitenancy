package com.simitchiyski.multitenant.core.admin.tenant;

import com.simitchiyski.multitenant.config.db.DefaultDataSourceBasedMultiTenantConnectionProvider;
import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@Transactional(transactionManager = "adminTransactionManager")
public class DefaultTenantService implements TenantService {
    private final TenantRepository tenantRepository;
    private final DefaultDataSourceBasedMultiTenantConnectionProvider defaultDataSourceBasedMultiTenantConnectionProvider;

    @Override
    @Transactional(readOnly = true)
    public @NotNull List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public @NotNull Tenant create(final @NotNull @Valid TenantCreateDto dto) {
        Tenant entity = new Tenant();

        final String name = dto.getName();
        entity.setName(name);

        final TenantDataSourceConfig tenantDataSourceConfig = new TenantDataSourceConfig();
        tenantDataSourceConfig.setDriverClassName("org.h2.Driver");
        tenantDataSourceConfig.setUrl("jdbc:h2:mem:" + name);
        tenantDataSourceConfig.setUsername(name);
        tenantDataSourceConfig.setPassword(name);
        tenantDataSourceConfig.setTenant(entity);

        entity.setTenantDataSourceConfig(tenantDataSourceConfig);
        entity = tenantRepository.save(entity);

        final Tenant finalEntity = entity;
        new Thread(() -> defaultDataSourceBasedMultiTenantConnectionProvider.addDataSource(finalEntity)).start();

        return finalEntity;
    }
}
