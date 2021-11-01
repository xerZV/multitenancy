package com.simitchiyski.multitenant.web.tenant.app.setting.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class AppSettingCreateDto {
    @NotNull
    @Size(min = 3, max = 50)
    String key;

    @NotNull
    @Size(min = 3, max = 50)
    String value;

    @NotNull
    @Size(min = 3, max = 100)
    String type;
}
