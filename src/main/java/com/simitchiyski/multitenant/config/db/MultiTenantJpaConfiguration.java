package com.simitchiyski.multitenant.config.db;

import com.simitchiyski.multitenant.config.tenant.DefaultCurrentTenantIdentifierResolver;
import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hibernate.MultiTenancyStrategy.DATABASE;

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

    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(final MultiTenantConnectionProvider multiTenantConnectionProvider,
                                                                             final CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
        final Map<String, Object> hibernateProps = new LinkedHashMap<>(jpaProperties.getProperties());
        hibernateProps.put(Environment.MULTI_TENANT, DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);

        // No dataSource is set to resulting entityManagerFactoryBean
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("tenant-persistence-unit");
        em.setPackagesToScan("com.simitchiyski.multitenant.core.tenant");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaPropertyMap(hibernateProps);

        return em;
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager tenantTxManager(final @Qualifier("tenantEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
