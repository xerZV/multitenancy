package com.simitchiyski.multitenant.core.tenant.app.setting;

import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
@Transactional(transactionManager = "tenantTransactionManager")
public class DefaultAppSettingService implements AppSettingService {
    private final AppSettingRepository appSettingRepository;

    @Override
    @Transactional(readOnly = true)
    public @NotNull List<AppSetting> findAll() {
        return appSettingRepository.findAll();
    }

    @Override
    public @NotNull AppSetting create(final @NotNull @Valid AppSettingCreateDto dto) {
        final AppSetting entity = new AppSetting();

        entity.setKey(dto.getKey());
        entity.setValue(dto.getValue());
        entity.setType(dto.getType());

        return appSettingRepository.save(entity);
    }
}
