package com.simitchiyski.multitenant.core.tenant.app.setting;

import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface AppSettingService {
    @NotNull List<AppSetting> findAll();
    @NotNull AppSetting create(final @NotNull @Valid AppSettingCreateDto dto);
}
