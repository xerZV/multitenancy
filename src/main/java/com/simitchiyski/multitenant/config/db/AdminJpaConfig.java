package com.simitchiyski.multitenant.config.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "adminTransactionManager",
        basePackages = {"com.simitchiyski.multitenant.core.admin"}
)
public class AdminJpaConfig {

    private final JpaProperties jpaProperties;

    @Primary
    @Bean(name = "adminDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource adminDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "adminEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean adminEntityManagerFactory(final @Qualifier("adminDataSource") DataSource adminDataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(adminDataSource);
        em.setPersistenceUnitName("admin-persistence-unit");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setPackagesToScan("com.simitchiyski.multitenant.core.admin");
        em.setJpaPropertyMap(new LinkedHashMap<>(this.jpaProperties.getProperties()));

        return em;
    }

    @Primary
    @Bean(name = "adminTransactionManager")
    public PlatformTransactionManager adminTransactionManager(
            final @Qualifier("adminEntityManagerFactory") EntityManagerFactory adminEntityManagerFactory) {
        return new JpaTransactionManager(adminEntityManagerFactory);
    }

    @Bean("adminDataSourceInitializer")
    public DataSourceInitializer adminDataSourceInitializer(final @Qualifier("adminDataSource") DataSource adminDataSource) {
        final ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("db/init/admin/schema.sql"));
        resourceDatabasePopulator.addScript(new ClassPathResource("db/init/admin/data.sql"));

        final DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(adminDataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
