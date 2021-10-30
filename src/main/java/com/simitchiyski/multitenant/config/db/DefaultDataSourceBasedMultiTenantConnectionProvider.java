package com.simitchiyski.multitenant.config.db;

import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantDataSourceConfig;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.service.spi.Stoppable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public class DefaultDataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
        implements Stoppable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private TenantRepository tenantRepository;

    private final Map<String, DataSource> tenantDataSources = new HashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        return tenantDataSources.values().iterator().next(); //TODO
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        log.info("obtaining dataSource for tenantId={}", tenantId);

        if (!tenantDataSources.containsKey(tenantId)) {
            final Tenant tenant = tenantRepository.findByName(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));

            tenantDataSources.put(tenant.getName(), getDataSource(tenant));
        }

        return tenantDataSources.get(tenantId);
    }

    @Override
    public void stop() {
        if (nonNull(tenantDataSources)) {
            tenantDataSources.clear();
        }
    }

    public void addDataSource(final Tenant tenant) {
        if (!tenantDataSources.containsKey(tenant.getName())) {
            tenantDataSources.put(tenant.getName(), getDataSource(tenant));
        }
    }

    private DataSource initialize(String tenantIdentifier, DataSource dataSource) {
        final ClassPathResource schemaResource = new ClassPathResource("db/init/tenants/schema.sql");
        ClassPathResource dataResource = new ClassPathResource(String.format("db/init/tenants/%s/data.sql", tenantIdentifier));

        ResourceDatabasePopulator populator;
        if (!dataResource.exists()) {
            populator = new ResourceDatabasePopulator(schemaResource);
        } else {
            populator = new ResourceDatabasePopulator(schemaResource, dataResource);
        }

        populator.execute(dataSource);

        return dataSource;
    }

    private DataSource getDataSource(final Tenant tenant) {
        final TenantDataSourceConfig tenantDataSourceConfig = tenant.getTenantDataSourceConfig();
        final DataSource tenantDataSource = initialize(tenant.getName(),
                DataSourceBuilder.create(getClass().getClassLoader())
                        .url(tenantDataSourceConfig.getUrl())
                        .username(tenantDataSourceConfig.getUsername())
                        .password(tenantDataSourceConfig.getPassword())
                        .driverClassName(tenantDataSourceConfig.getDriverClassName())
                        .build()
        );
        return tenantDataSource;
    }
}
