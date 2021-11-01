package com.simitchiyski.multitenant;

import com.simitchiyski.multitenant.core.admin.tenant.Tenant;
import com.simitchiyski.multitenant.core.admin.tenant.TenantRepository;
import com.simitchiyski.multitenant.core.tenant.app.setting.AppSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.simitchiyski.multitenant.config.tenant.TenantContext.withTenant;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

    private final TenantRepository tenantRepository;
    private final AppSettingRepository appSettingRepository;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        final List<Tenant> all = tenantRepository.findAll();
        log.info("fetched tenants={}", all);

        for (final Tenant tenant : all) {
            withTenant(tenant.getName(), () -> {
                log.info("tenant={}, appSetting={}", tenant.getName(), appSettingRepository.findAll());
                return null;
            });
        }
    }
}
