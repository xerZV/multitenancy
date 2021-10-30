package com.simitchiyski.multitenant.core.admin.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantDataSourceConfigRepository extends JpaRepository<TenantDataSourceConfig, Long> {
}
