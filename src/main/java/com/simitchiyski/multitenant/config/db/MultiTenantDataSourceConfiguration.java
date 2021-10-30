package com.simitchiyski.multitenant.config.db;

import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantDataSourceConfig;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Configuration //TODO
@RequiredArgsConstructor
public class MultiTenantDataSourceConfiguration {

    private final TenantRepository tenantRepository;

    @Bean(name = "tenantDataSources")
    public Map<String, DataSource> tenantDataSources() {
        final List<Tenant> tenants = tenantRepository.findAll();
        final Map<String, DataSource> tenantDataSources = new HashMap<>(tenants.size());

        for (final Tenant tenant : tenants) {
            final TenantDataSourceConfig tenantDataSourceConfig = tenant.getTenantDataSourceConfig();
            final DataSource tenantDataSource = initialize(tenant.getName(),
                    DataSourceBuilder.create(getClass().getClassLoader())
                            .url(tenantDataSourceConfig.getUrl())
                            .username(tenantDataSourceConfig.getUsername())
                            .password(tenantDataSourceConfig.getPassword())
                            .driverClassName(tenantDataSourceConfig.getDriverClassName())
                            .build()
            );

            tenantDataSources.put(tenant.getName(), tenantDataSource);
        }

        return tenantDataSources;
    }

    private DataSource initialize(String tenantIdentifier, DataSource dataSource) {
        final ClassPathResource schemaResource = new ClassPathResource("db/init/tenants/schema.sql");
        final ClassPathResource dataResource = new ClassPathResource(String.format("db/init/tenants/%s/data.sql", tenantIdentifier));
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator(schemaResource, dataResource);

        populator.execute(dataSource);

        return dataSource;
    }
}
