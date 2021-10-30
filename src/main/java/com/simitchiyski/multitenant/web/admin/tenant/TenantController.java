package com.simitchiyski.multitenant.web.admin.tenant;

import com.simitchiyski.multitenant.config.db.DefaultDataSourceBasedMultiTenantConnectionProvider;
import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantDataSourceConfig;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import com.simitchiyski.multitenant.web.admin.tenant.dto.TenantCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/tenants")
public class TenantController {
    private final TenantRepository tenantRepository;
    private final DefaultDataSourceBasedMultiTenantConnectionProvider defaultDataSourceBasedMultiTenantConnectionProvider;

    @GetMapping
    public ResponseEntity<List<Tenant>> tenantAppSettings() {
        return ok(tenantRepository.findAll());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody TenantCreateDto dto) {
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

        return ok(entity);
    }
}
