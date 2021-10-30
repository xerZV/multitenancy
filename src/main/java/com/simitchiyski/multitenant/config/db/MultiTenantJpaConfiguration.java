package com.simitchiyski.multitenant.config.db;

import com.simitchiyski.multitenant.config.tenant.DefaultCurrentTenantIdentifierResolver;
import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantDataSourceConfig;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager",
        basePackages = {"com.simitchiyski.multitenant.core.tenant"}
)
public class MultiTenantJpaConfiguration {

    private final JpaProperties jpaProperties;

    @Bean
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DefaultDataSourceBasedMultiTenantConnectionProvider();
    }

    @Bean
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new DefaultCurrentTenantIdentifierResolver();
    }

    @Bean(name = "tenantEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(final MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                           final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
        final Map<String, Object> hibernateProps = new LinkedHashMap<>(jpaProperties.getProperties());
        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // No dataSource is set to resulting entityManagerFactoryBean
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setPackagesToScan("");
        em.setPackagesToScan(new String[]{"com.simitchiyski.multitenant.core.tenant",
                "com.simitchiyski.multitenant.core.tenant.app.setting"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(hibernateProps);

        return em;
    }

    @Bean(name = "tenantEntityManagerFactory")
    public EntityManagerFactory tenantEntityManagerFactory(
            final @Qualifier("tenantEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return entityManagerFactoryBean.getObject();
    }

    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager txManager(final @Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager jpa = new JpaTransactionManager();

        jpa.setEntityManagerFactory(entityManagerFactory);

        return jpa;
    }
}
