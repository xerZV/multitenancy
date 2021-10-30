package com.simitchiyski.multitenant.web.tenant.app.setting.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AppSettingCreateDto {
    @NotNull
    @Size(min = 3, max = 50)
    private String key;

    @NotNull
    @Size(min = 3, max = 50)
    private String value;

    @NotNull
    @Size(min = 3, max = 100)
    private String type;
}
