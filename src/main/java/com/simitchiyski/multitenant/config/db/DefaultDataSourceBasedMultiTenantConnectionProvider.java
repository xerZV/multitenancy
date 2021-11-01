package com.simitchiyski.multitenant.config.db;

import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantDataSourceConfig;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.hibernate.service.spi.Stoppable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.simitchiyski.multitenant.config.tenant.DefaultCurrentTenantIdentifierResolver.DEFAULT;

@Slf4j
@Component
public class DefaultDataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
        implements Stoppable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private TenantRepository tenantRepository;

    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        log.info("selecting any DataSource");

        if (tenantDataSources.isEmpty()) {
            tenantRepository.findAll().forEach(tenant -> tenantDataSources.put(tenant.getName(), getDataSource(tenant)));
        }

        return tenantDataSources.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantId) {
        log.info("obtaining dataSource for tenantId={}", tenantId);
        if (DEFAULT.equalsIgnoreCase(tenantId)) {
            final RuntimeException runtimeException = new RuntimeException(String.format("identifier=%s is not a tenant.", DEFAULT));
            log.error("obtaining dataSource for tenantId={}", tenantId, runtimeException);
            throw runtimeException;
        }

        if (!tenantDataSources.containsKey(tenantId)) {
            final Tenant tenant = tenantRepository.findByName(tenantId).orElseThrow(() -> new EntityNotFoundException("Tenant does not exist"));

            tenantDataSources.put(tenant.getName(), getDataSource(tenant));
        }

        return tenantDataSources.get(tenantId);
    }

    @Override
    public void stop() {
        tenantDataSources.clear();
    }

    public void addDataSource(final Tenant tenant) {
        if (!tenantDataSources.containsKey(tenant.getName())) {
            tenantDataSources.put(tenant.getName(), getDataSource(tenant));
        }
    }

    private DataSource initialize(String tenantIdentifier, DataSource dataSource) {
        //LiquibaseRunner.runMigration(hds, dbName); // TODO use Liquibase

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

        return initialize(tenant.getName(),
                DataSourceBuilder.create(getClass().getClassLoader())
                        .url(tenantDataSourceConfig.getUrl())
                        .username(tenantDataSourceConfig.getUsername())
                        .password(tenantDataSourceConfig.getPassword())
                        .driverClassName(tenantDataSourceConfig.getDriverClassName())
                        .build()
        );
    }
}
