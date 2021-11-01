package com.simitchiyski.multitenant.web.tenant.app.setting.mapper;

import com.simitchiyski.multitenant.core.abstraction.mapper.EntityToDtoMapper;
import com.simitchiyski.multitenant.core.tenant.app.setting.AppSetting;
import com.simitchiyski.multitenant.web.tenant.app.setting.dto.AppSettingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppSettingMapper extends EntityToDtoMapper<AppSetting, AppSettingDto> {
}
