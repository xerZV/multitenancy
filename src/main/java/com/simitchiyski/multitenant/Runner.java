package com.simitchiyski.multitenant;

import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

    private final TenantRepository tenantRepository;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final List<Tenant> all = tenantRepository.findAll();
        log.info("fetched tenants={}", all);
    }
}
